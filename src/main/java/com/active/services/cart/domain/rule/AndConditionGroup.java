package com.active.services.cart.domain.rule;

public class AndConditionGroup extends ConditionGroup {
    public AndConditionGroup and(Condition...conditions) {
        addConditions(conditions);
        return this;
    }

    @Override
    protected boolean test(Fact fact) {
        return conditions().stream().allMatch(cond -> cond.satisfy(fact));
    }
}
