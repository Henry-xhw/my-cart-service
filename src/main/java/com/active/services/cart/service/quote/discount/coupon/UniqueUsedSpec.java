package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UniqueUsedSpec implements DiscountSpecification {
    private final Long discountId;

    private final List<Long> usedDiscountIds;

    @Override
    public boolean satisfy() {
        return !usedDiscountIds.contains(discountId);
    }
}
