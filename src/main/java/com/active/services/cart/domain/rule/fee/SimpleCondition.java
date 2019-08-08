package com.active.services.cart.domain.rule.fee;

import com.active.services.cart.domain.rule.Fact;

public class SimpleCondition extends SingleFieldCondition {
    private final String value;

    public SimpleCondition(String field, String value) {
        super(field);
        this.value = value;
    }

    @Override
    public boolean test(Fact fact) {
        return fact(fact).filter(f -> f.equals(value)).isPresent();
    }
}
