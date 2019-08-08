package com.active.services.cart.domain.rule.fee;

import java.util.Optional;

import com.active.services.cart.domain.rule.Condition;
import com.active.services.cart.domain.rule.Fact;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class FeeCondition implements Condition {
    private final String field;
    private boolean reverse;

    protected abstract boolean test(Fact fact);

    protected <T> Optional<T> fact(Fact fact) {
        return Optional.ofNullable(fact.getFact(field));
    }

    public boolean satisfy(Fact fact) {
        return reverse != test(fact);
    }
}
