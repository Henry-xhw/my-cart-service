package com.active.services.cart.domain.discount;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscountSequentialCondition implements DiscountCondition {
    private List<DiscountCondition> conditions;

    public static DiscountSequentialCondition allOf(DiscountCondition...conditions) {
        DiscountSequentialCondition ds = new DiscountSequentialCondition();
        ds.conditions = new ArrayList<>(Arrays.asList(conditions));
        return ds;
    }
}
