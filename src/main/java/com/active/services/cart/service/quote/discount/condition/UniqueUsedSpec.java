package com.active.services.cart.service.quote.discount.condition;

import com.active.services.DiscountModel;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class UniqueUsedSpec implements DiscountSpecification {

    private final DiscountModel discountModel;
    private final Long discountId;
    private final List<Long> usedDiscountIds;

    @Override
    public boolean satisfy() {
        return discountModel == DiscountModel.COMBINABLE_FLAT_FIRST || !usedDiscountIds.contains(discountId);
    }
}
