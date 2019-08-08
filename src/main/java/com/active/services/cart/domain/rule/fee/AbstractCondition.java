package com.active.services.cart.domain.rule.fee;

import com.active.services.cart.domain.rule.Condition;
import com.active.services.cart.domain.rule.Fact;

public abstract class AbstractCondition implements Condition {
    private boolean reverse;

    protected abstract boolean test(Fact fact);

    @Override
    public boolean satisfy(Fact fact) {
        return reverse != test(fact);
    }

    @Override
    public Condition reverse() {
        reverse = !reverse;
        return this;
    }
}
