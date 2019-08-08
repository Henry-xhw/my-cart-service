package com.active.services.cart.domain.rule.fee;

import com.active.services.cart.domain.rule.Fact;

public class SimpleFeeCondition extends FeeCondition {
    private final String value;
    private ConditionOperator operator;

    public SimpleFeeCondition(String field, String value, ConditionOperator operator) {
        super(field);
        this.value = value;
        this.operator = operator;
    }

    @Override
    public boolean test(Fact fact) {
        return fact(fact).filter(f -> f.equals(value)).isPresent();
    }
}
