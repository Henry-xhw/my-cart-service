package com.active.services.cart.service.quote.discount.condition;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UniqueSpec implements DiscountSpecification {
    private final List<Long> appliedDiscountIds;

    private final Long discountId;

    @Override
    public boolean satisfy() {
        return !appliedDiscountIds.contains(discountId);
    }
}
