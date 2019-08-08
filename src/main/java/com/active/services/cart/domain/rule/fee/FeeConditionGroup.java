package com.active.services.cart.domain.rule.fee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.active.services.cart.domain.rule.Condition;
import com.active.services.cart.domain.rule.Fact;

public class FeeConditionGroup implements Condition {
    private List<Condition> conditions = new ArrayList<>();
    private List<FeeConditionGroup> groups = new ArrayList<>();

    private ConditionGroupOperator groupOperator;

    public FeeConditionGroup and(Condition...conditions) {
        groupOperator = ConditionGroupOperator.AND;
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    public FeeConditionGroup or(Condition...conditions) {
        groupOperator = ConditionGroupOperator.OR;
        this.conditions.addAll(Arrays.asList(conditions));
        return this;
    }

    @Override
    public boolean satisfy(Fact fact) {
        if (groupOperator == ConditionGroupOperator.AND) {
            return conditions.stream().allMatch(cond -> cond.satisfy(fact));
        }
        return conditions.stream().anyMatch(cond -> cond.satisfy(fact));
    }
}
