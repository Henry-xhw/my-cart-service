package com.active.services.cart.domain.rule;

public interface Rule {
    String getIdentifier();

    boolean evaluate(Fact fact);

    void doAction(Fact fact);

    String getName();

    int getPriority();

    String ruleExpression();

    boolean isExclusive();
}
