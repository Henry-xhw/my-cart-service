package com.active.services.cart.domain.rule.fee;

public class LongRangeCondition extends RangeCondition<Long> {
    public LongRangeCondition(String field, Long start, Long end) {
        super(field, start, end);
    }
}
