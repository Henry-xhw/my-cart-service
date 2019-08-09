package com.active.services.cart.domain.rule;

public interface Action<T extends Fact> {
    void doAction(T fact);
}
