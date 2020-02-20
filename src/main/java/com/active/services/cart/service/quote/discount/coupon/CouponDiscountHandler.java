package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.DiscountModel;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.algorithm.BestDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.StackableFlatFirstDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.domain.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.domain.Discount;
import com.active.services.cart.service.quote.discount.processor.DiscountHandler;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CouponDiscountHandler implements DiscountHandler {

    @NonNull private final CartQuoteContext context;
    @NonNull private final CartItemDiscounts itemDiscounts;

    @Override
    public List<Discount> filterDiscounts() {
        List<Discount> discounts = itemDiscounts.getCouponDiscounts().stream()
                .filter(Discount::satisfy)
                .collect(Collectors.toList());
        return getHighPriorityDiscounts(discounts);
    }

    @Override
    public DiscountAlgorithm getDiscountAlgorithm() {
        return context.getDiscountModel(itemDiscounts.getCartItem().getProductId()) == DiscountModel.COMBINABLE_FLAT_FIRST ?
                new StackableFlatFirstDiscountAlgorithm() :
                new BestDiscountAlgorithm(itemDiscounts.getCartItem(), context.getCurrency());
    }

    private List<Discount> getHighPriorityDiscounts(List<Discount> discounts) {
        List<Discount> cartItemLevelDiscount = new ArrayList<>();
        if (itemDiscounts.getCartItem().getCouponMode() == CouponMode.HIGH_PRIORITY) {
            cartItemLevelDiscount =
                    CollectionUtils.emptyIfNull(discounts).stream().filter(discount ->
                            itemDiscounts.getCartItem().getCouponCodes().contains(discount.getCouponCode())).collect(Collectors.toList());
        }
        return CollectionUtils.isNotEmpty(cartItemLevelDiscount) ?  cartItemLevelDiscount : discounts;
    }
}

