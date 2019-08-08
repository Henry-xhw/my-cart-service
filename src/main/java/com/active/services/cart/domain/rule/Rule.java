package com.active.services.cart.domain.rule;

public interface Rule {
    String getIdentifier();

    boolean fire(Fact fact);

    void doAction(Fact fact);

    String getName();

    int getPriority();

    String ruleExpression();
}
