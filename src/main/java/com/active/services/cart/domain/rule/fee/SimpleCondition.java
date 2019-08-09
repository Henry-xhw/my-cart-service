package com.active.services.cart.domain.rule.fee;

import com.active.services.cart.domain.rule.Fact;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SimpleCondition extends SingleFieldCondition {
    private final String value;

    public SimpleCondition(String field, String value) {
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
