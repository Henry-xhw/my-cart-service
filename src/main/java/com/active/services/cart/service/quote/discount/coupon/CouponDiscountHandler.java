package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountApplication;
import com.active.services.cart.service.quote.discount.DiscountHandler;
import com.active.services.cart.service.quote.discount.algorithm.BestDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.StackableFlatFirstDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.cart.service.quote.discount.condition.UniqueUsedSpec;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CouponDiscountHandler implements DiscountHandler {

    @NonNull private final CartQuoteContext context;
    @NonNull private final CartItemDiscounts itemDiscounts;

    /**
     * Coupon code discounts should match all discounts conditions.
     * {@link com.active.services.cart.service.quote.discount.condition.NotExpiredSpec}
     * {@link com.active.services.cart.service.quote.discount.condition.UniqueUsedSpec}
     *
     * When discount mode is COMBINABLE_FLAT_FIRST, all discounts are satisfy.
     * Otherwise, high priority discounts {@link #getHighPriorityDiscounts} will be returned.
     */
    @Override
    public List<DiscountApplication> filterDiscounts() {

        CollectionUtils.emptyIfNull(itemDiscounts.getDiscounts()).stream().forEach(discountApplication ->
                discountApplication.setCondition(buildDiscountSpecification(context, discountApplication)));

        List<DiscountApplication> discounts = itemDiscounts.getDiscounts().stream()
                .filter(DiscountApplication::satisfy)
                .collect(Collectors.toList());

        return isCombinableDiscountMode() ? discounts : getHighPriorityDiscounts(discounts);
    }

    @Override
    public DiscountAlgorithm getDiscountAlgorithm() {
        return isCombinableDiscountMode() ?
                new StackableFlatFirstDiscountAlgorithm() :
                new BestDiscountAlgorithm(itemDiscounts.getCartItem(), context.getCurrency());
    }

    /**
     * Determines which given discounts with high priority.
     * When cart item with cart item level discount and CouponMode is HIGH_PRIORITY, return cart item level discounts.
     * Otherwise, return given discounts.
     *
     */
    private List<DiscountApplication> getHighPriorityDiscounts(List<DiscountApplication> discounts) {
        List<DiscountApplication> cartItemLevelDiscount = new ArrayList<>();
        if (itemDiscounts.getCartItem().getCouponMode() == CouponMode.HIGH_PRIORITY) {
            cartItemLevelDiscount =
                    CollectionUtils.emptyIfNull(discounts).stream().filter(discount ->
                            itemDiscounts.getCartItem().getCouponCodes().contains(discount.getCouponCode()))
                            .collect(Collectors.toList());
        }
        return CollectionUtils.isNotEmpty(cartItemLevelDiscount) ?  cartItemLevelDiscount : discounts;
    }

    private boolean isCombinableDiscountMode() {
        return context.getDiscountModel(itemDiscounts.getCartItem().getProductId()) == DiscountModel.COMBINABLE_FLAT_FIRST;
    }


    private static DiscountSpecification buildDiscountSpecification(CartQuoteContext context, Discount disc) {
        return DiscountSequentialSpecs.allOf(
                new NotExpiredSpec(disc.getStartDate(), disc.getEndDate(), Instant.now()),
                new UniqueUsedSpec(disc.getDiscountId(), context.getUsedUniqueCouponDiscountsIds())
        );
    }
}

