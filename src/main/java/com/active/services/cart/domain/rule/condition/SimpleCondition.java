package com.active.services.cart.domain.rule.condition;

import com.active.services.cart.domain.rule.Fact;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleCondition<T> extends SingleFieldCondition {
    private final T value;

    public SimpleCondition(String field, T value) {
        super(field);
        this.value = value;
    }

    @Override
    public boolean test(Fact fact) {
        boolean conditionMet = fact(fact).filter(f -> f.equals(value)).isPresent();
        LOG.debug("{} == {} return {}", fact(fact), value, conditionMet);
        return conditionMet;
    }
}
