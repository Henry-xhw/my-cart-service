package com.active.services.cart.domain.rule.fee;

import java.util.Optional;

import com.active.services.cart.domain.rule.Fact;

public class LongRangeCondition extends SingleFieldCondition {
    private final Long start;
    private final Long end;

    public LongRangeCondition(String field, Long start, Long end) {
        super(field);
        this.start = start;
        this.end = end;
    }

    @Override
    protected boolean test(Fact fact) {
        Optional<Long> number = fact(fact);
        return number.filter(n -> (start == null || start.compareTo(n) >= 0) && (end == null || n.compareTo(end) < 0)).isPresent();
    }
}
