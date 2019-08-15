package com.active.services.cart.domain.rule.condition;

import com.active.services.cart.domain.rule.Fact;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class RangeCondition<T extends Comparable> extends SingleFieldCondition {
    private T fromInclusive;
    private T toExclusive;

    public RangeCondition(String field, T fromInclusive, T toExclusive) {
        super(field);
        this.fromInclusive = fromInclusive;
        this.toExclusive = toExclusive;
    }

    @Override
    public boolean test(Fact fact) {
        Optional<T> optFact = fact(fact);
        boolean conditionMet = optFact.filter(f -> (fromInclusive == null || f.compareTo(fromInclusive) >= 0) &&
                (toExclusive == null || f.compareTo(toExclusive) < 0)).isPresent();

        LOG.debug("{} <= {} < {} return {}", fromInclusive, optFact, toExclusive, conditionMet);
        return conditionMet;
    }
}
