package com.active.services.cart.domain.rule.condition;

public class LongRangeCondition extends RangeCondition<Long> {
    public LongRangeCondition(String field, Long start, Long end) {
        super(field, start, end);
    }
}
