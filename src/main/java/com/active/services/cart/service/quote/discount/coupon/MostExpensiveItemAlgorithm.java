package com.active.services.cart.service.quote.discount.coupon;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MostExpensiveItemAlgorithm {

    private final List<CartItemEffectiveCouponBuilder> rawBuilders;

    public List<CartItemEffectiveCouponBuilder> apply() {
        return null;
    }
}
