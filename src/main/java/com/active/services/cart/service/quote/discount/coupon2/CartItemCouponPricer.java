package com.active.services.cart.service.quote.discount.coupon2;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscountBasePricer;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountFeeLoader;
import com.active.services.cart.service.quote.discount.algorithm.BestDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.StackableFlatFirstDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.cart.service.quote.discount.condition.UniqueUsedSpec;
import com.active.services.cart.service.quote.discount.condition.UsageLimitSpec;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemCouponPricer extends CartItemDiscountBasePricer {

    private final CouponDiscountContext couponDiscountContext;

    private final CartItemDiscounts itemDiscounts;

    @Override
    protected void doQuote(CartQuoteContext context, CartItem cartItem) {
        List<Discount> discounts = CollectionUtils.emptyIfNull(itemDiscounts.getDiscounts()).stream()
                .filter(discount -> satisfy(discount, context)).collect(Collectors.toList());
        discounts = isCombinableDiscountMode(context) ? discounts : getHighPriorityDiscounts(discounts);

        if (CollectionUtils.isEmpty(discounts)) {
            return;
        }
        if (cartItem.getNetPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }

        getDiscountAlgorithm(context).apply(discounts).forEach(disc ->
                new DiscountFeeLoader(context, cartItem, disc).load());
    }

    private DiscountAlgorithm getDiscountAlgorithm(CartQuoteContext context) {
        return isCombinableDiscountMode(context) ?
                new StackableFlatFirstDiscountAlgorithm() :
                new BestDiscountAlgorithm(itemDiscounts.getCartItem(), context.getCurrency());
    }

    /**
     * Determines which given discounts with high priority.
     * When cart item with cart item level discount and CouponMode is HIGH_PRIORITY, return cart item level discounts.
     * Otherwise, return given discounts.
     *
     */
    private List<Discount> getHighPriorityDiscounts(List<Discount> discounts) {
        List<Discount> cartItemLevelDiscount = new ArrayList<>();
        if (itemDiscounts.getCartItem().getCouponMode() == CouponMode.HIGH_PRIORITY) {
            cartItemLevelDiscount =
                    CollectionUtils.emptyIfNull(discounts).stream().filter(discount ->
                            itemDiscounts.getCartItem().getCouponCodes().contains(discount.getCouponCode()))
                            .collect(Collectors.toList());
        }
        return CollectionUtils.isNotEmpty(cartItemLevelDiscount) ?  cartItemLevelDiscount : discounts;
    }

    private boolean isCombinableDiscountMode(CartQuoteContext context) {
        return context.getDiscountModel(
                itemDiscounts.getCartItem().getProductId()) == DiscountModel.COMBINABLE_FLAT_FIRST;
    }

    private boolean satisfy(Discount discount, CartQuoteContext context) {
        DiscountSequentialSpecs specification = DiscountSequentialSpecs.allOf(
                new NotExpiredSpec(discount.getStartDate(), discount.getEndDate(), Instant.now()),
                new UsageLimitSpec(couponDiscountContext.getDiscountUsages(), discount.getDiscountId()));

        if (discount.getAlgorithm() == com.active.services.product.DiscountAlgorithm.MOST_EXPENSIVE) {
            specification.addSpecification(new UniqueUsedSpec(discount.getDiscountId(),
                    couponDiscountContext.getUsedUniqueCouponDiscountsIds(context.getAppliedDiscounts())));
        }

        return specification.satisfy();
    }
}
