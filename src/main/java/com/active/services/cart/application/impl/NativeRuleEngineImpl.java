package com.active.services.cart.application.impl;

import java.util.Comparator;
import java.util.List;

import com.active.services.cart.application.RuleEngine;
import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.Rule;

public class NativeRuleEngineImpl implements RuleEngine {
    @Override
    public void runRule(List<?> facts) {
    }

    @Override
    public void runRules(List<Rule> rules, Fact fact) {
        rules.sort(Comparator.comparingInt(Rule::getPriority));

        for (Rule r : rules) {
            if (r.evaluate(fact)) {
                r.doAction(fact);
                if (r.isExclusive()) {
                    break;
                }
            }
        }
    }
}
