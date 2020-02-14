package com.active.services.cart.service.quote.discount.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscountSequentialSpecs implements DiscountSpecification {
    private List<DiscountSpecification> conditions = new ArrayList<>(0);

    public static DiscountSequentialSpecs allOf(DiscountSpecification...conditions) {
        DiscountSequentialSpecs ds = new DiscountSequentialSpecs();
        ds.conditions.addAll(Arrays.asList(conditions));
        return ds;
    }

    @Override
    public boolean satisfy() {
        return conditions.stream().allMatch(DiscountSpecification::satisfy);
    }
}
