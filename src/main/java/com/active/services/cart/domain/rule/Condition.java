package com.active.services.cart.domain.rule;

public interface Condition {
    boolean satisfy(Fact fact);
}
