package com.active.services.cart.domain.discount.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscountOrSpec implements DiscountSpecification {
    private List<DiscountSpecification> conditions = new ArrayList<>(0);

    public static DiscountOrSpec anyOf(DiscountSpecification...conditions) {
        DiscountOrSpec ds = new DiscountOrSpec();
        ds.conditions.addAll(Arrays.asList(conditions));
        return ds;
    }

    @Override
    public boolean satisfy() {
        return conditions.stream().anyMatch(DiscountSpecification::satisfy);
    }
}
