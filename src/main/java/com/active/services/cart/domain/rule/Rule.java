package com.active.services.cart.domain.rule;

public interface Rule {
    boolean evaluate(Fact fact);

    void doAction(Fact fact);

    String getName();

    int getPriority();

    boolean isExclusive();
}
