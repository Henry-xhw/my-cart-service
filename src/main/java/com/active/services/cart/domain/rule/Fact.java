package com.active.services.cart.domain.rule;

public interface Fact {
    <T> T getFact(String key);
}
