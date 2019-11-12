package com.active.services.cart.domain.discount.condition;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiscountCodeSpecification implements DiscountSpecification {
    private final String desired;
    private final String input;

    @Override
    public boolean satisfy() {
        return desired == null || desired.equalsIgnoreCase(input);
    }
}
