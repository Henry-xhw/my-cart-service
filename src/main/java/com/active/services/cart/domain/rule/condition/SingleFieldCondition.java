package com.active.services.cart.domain.rule.condition;

import com.active.services.cart.domain.rule.AbstractCondition;
import com.active.services.cart.domain.rule.Fact;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class SingleFieldCondition extends AbstractCondition {
    private final String field;

    protected <T> Optional<T> fact(Fact fact) {
        return Optional.ofNullable(fact.getFact(field));
    }
}
