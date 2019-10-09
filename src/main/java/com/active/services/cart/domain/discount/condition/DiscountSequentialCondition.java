package com.active.services.cart.domain.discount.condition;

import com.active.services.cart.domain.discount.DiscountCondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscountSequentialCondition implements DiscountCondition {
    private List<DiscountCondition> conditions = new ArrayList<>(0);

    public static DiscountSequentialCondition allOf(DiscountCondition...conditions) {
        DiscountSequentialCondition ds = new DiscountSequentialCondition();
        ds.conditions.addAll(Arrays.asList(conditions));
        return ds;
    }

    @Override
    public boolean satisfy() {
        return conditions.stream().allMatch(DiscountCondition::satisfy);
    }
}
