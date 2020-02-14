package com.active.services.cart.service.quote.discount.condition;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CouponCodeSpec implements DiscountSpecification {
    private final String desired;
    private final String input;

    @Override
    public boolean satisfy() {
        return desired == null || desired.equalsIgnoreCase(input);
    }
}
