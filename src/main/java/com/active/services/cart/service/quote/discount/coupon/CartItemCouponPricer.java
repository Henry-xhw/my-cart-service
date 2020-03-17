package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscountBasePricer;
import com.active.services.cart.service.quote.discount.DiscountFeeLoader;
import com.active.services.cart.service.quote.discount.DiscountMapper;
import com.active.services.cart.service.quote.discount.algorithm.BestDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.StackableFlatFirstDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.product.Discount;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemCouponPricer extends CartItemDiscountBasePricer {

    private final CouponDiscountContext couponDiscountContext;

    private final List<Discount> itemDiscounts;

    @Override
    protected void doQuote(CartQuoteContext context, CartItem cartItem) {
        List<com.active.services.product.Discount> couponDiscounts = itemDiscounts.stream()
                .filter(discount -> satisfy(discount, context))
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(couponDiscounts)) {
            return;
        }
        couponDiscounts = isCombinableDiscountMode(cartItem, context) ? couponDiscounts :
                getHighPriorityDiscounts(cartItem, couponDiscounts);

        getDiscountAlgorithm(cartItem, context).apply(couponDiscounts.stream().map(discount ->
                DiscountMapper.MAPPER.toDiscount(discount, context))
                .collect(Collectors.toList())).forEach(disc ->
                new DiscountFeeLoader(context, cartItem, disc).load());
    }

    private DiscountAlgorithm getDiscountAlgorithm(CartItem cartItem, CartQuoteContext context) {
        return isCombinableDiscountMode(cartItem, context) ?
                new StackableFlatFirstDiscountAlgorithm() :
                new BestDiscountAlgorithm(cartItem, context.getCurrency());
    }

    /**
     * Determines which given discounts with high priority.
     * When cart item with cart item level discount and CouponMode is HIGH_PRIORITY, return cart item level discounts.
     * Otherwise, return given discounts.
     *
     */
    private List<Discount> getHighPriorityDiscounts(CartItem cartItem,
                                                    List<Discount> discounts) {
        List<Discount> cartItemLevelDiscount = new ArrayList<>();
        if (cartItem.getCouponMode() == CouponMode.HIGH_PRIORITY) {
            cartItemLevelDiscount =
                    CollectionUtils.emptyIfNull(discounts).stream().filter(discount ->
                            cartItem.getCouponCodes().contains(discount.getCouponCode()))
                            .collect(Collectors.toList());
        }
        return CollectionUtils.isNotEmpty(cartItemLevelDiscount) ?  cartItemLevelDiscount : discounts;
    }

    private boolean isCombinableDiscountMode(CartItem cartItem, CartQuoteContext context) {
        return context.getDiscountModel(
                cartItem.getProductId()) == DiscountModel.COMBINABLE_FLAT_FIRST;
    }

    private boolean satisfy(com.active.services.product.Discount discount, CartQuoteContext context) {
        DiscountSequentialSpecs specification = DiscountSequentialSpecs.allOf(
                new NotExpiredSpec(discount.getStartDate() == null ? null : discount.getStartDate().toDate().toInstant(),
                        discount.getEndDate() == null ? null : discount.getEndDate().toDate().toInstant(),
                        Instant.now()));

        Optional<DiscountUsage> usageOpt = couponDiscountContext.findDiscountUsageByDiscountId(discount.getId());
        if (usageOpt.isPresent()) {
            DiscountUsage discountUsage = usageOpt.get();
            specification.addSpecification(() -> discountUsage.getLimit() > discountUsage.getUsage());
        }

        if (discount.getDiscountAlgorithm() == com.active.services.product.DiscountAlgorithm.MOST_EXPENSIVE) {
            List<Long> appliedDiscountIds = couponDiscountContext
                    .getUsedUniqueCouponDiscountsIds(context.getAppliedDiscounts());
            specification.addSpecification(() -> 
                    !appliedDiscountIds.contains(discount.getId()));
        }

        return specification.satisfy();
    }
}
