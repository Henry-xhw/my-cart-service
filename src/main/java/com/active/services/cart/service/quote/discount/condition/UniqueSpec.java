package com.active.services.cart.service.quote.discount.condition;

import com.active.services.product.DiscountAlgorithm;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UniqueSpec implements DiscountSpecification {

    private final List<Long> appliedDiscountIds;

    private final Long discountId;

    private final DiscountAlgorithm algorithm;

    @Override
    public boolean satisfy() {
        return algorithm != DiscountAlgorithm.MOST_EXPENSIVE || !appliedDiscountIds.contains(discountId);
    }
}
