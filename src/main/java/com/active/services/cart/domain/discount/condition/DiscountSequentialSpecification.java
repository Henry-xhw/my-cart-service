package com.active.services.cart.domain.discount.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscountSequentialSpecification implements DiscountSpecification {
    private List<DiscountSpecification> conditions = new ArrayList<>(0);

    public static DiscountSequentialSpecification allOf(DiscountSpecification...conditions) {
        DiscountSequentialSpecification ds = new DiscountSequentialSpecification();
        ds.conditions.addAll(Arrays.asList(conditions));
        return ds;
    }

    @Override
    public boolean satisfy() {
        return conditions.stream().allMatch(DiscountSpecification::satisfy);
    }
}
