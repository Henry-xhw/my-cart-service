package com.active.services.cart.domain.discount.condition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscountOrSpecifications implements DiscountSpecification {
    private List<DiscountSpecification> conditions = new ArrayList<>(0);

    public static DiscountOrSpecifications anyOf(DiscountSpecification...conditions) {
        DiscountOrSpecifications ds = new DiscountOrSpecifications();
        ds.conditions.addAll(Arrays.asList(conditions));
        return ds;
    }

    @Override
    public boolean satisfy() {
        return conditions.stream().anyMatch(DiscountSpecification::satisfy);
    }
}
