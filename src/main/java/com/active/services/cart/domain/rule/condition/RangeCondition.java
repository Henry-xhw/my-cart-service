package com.active.services.cart.domain.rule.condition;

import java.util.Optional;

import com.active.services.cart.domain.rule.Fact;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RangeCondition<T extends Comparable> extends SingleFieldCondition {
    private T start;
    private T end;

    RangeCondition(String field, T start, T end) {
        super(field);
        this.start = start;
        this.end = end;
    }

    @Override
    public boolean test(Fact fact) {
        Optional<T> datetime = fact(fact);
        boolean conditionMet = datetime.filter(dt -> (start == null || dt.compareTo(start) >= 0) &&
                (end == null || dt.compareTo(end) < 0)).isPresent();

        LOG.debug("{} <= {} < {} return {}", start, datetime, end, conditionMet);
        return conditionMet;
    }
}
