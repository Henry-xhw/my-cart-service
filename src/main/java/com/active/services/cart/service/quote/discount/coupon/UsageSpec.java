package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UsageSpec implements DiscountSpecification {
    private final DiscountUsage discountUsage;

    @Override
    public boolean satisfy() {
        return discountUsage == null || discountUsage.getLimit() > discountUsage.getUsage();
    }
}
