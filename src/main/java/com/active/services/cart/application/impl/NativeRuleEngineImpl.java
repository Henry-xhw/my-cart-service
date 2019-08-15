package com.active.services.cart.application.impl;

import com.active.services.cart.application.RuleEngine;
import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.Rule;

import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class NativeRuleEngineImpl implements RuleEngine {

    @Override
    public boolean runRules(List<Rule> rules, Fact fact) {
        boolean anyRuleFired = false;

        rules.sort(Comparator.comparingInt(Rule::getPriority));
        for (Rule r : rules) {
            boolean toFire = r.evaluate(fact);
            LOG.debug("rule: {}, priority: {}, to fire: {}", r.getName(), r.getPriority(), toFire);
            if (toFire) {
                anyRuleFired = true;
                LOG.info("rule: {}, priority: {} fired, exclusive: {}", r.getName(), r.getPriority(), r.isExclusive());
                r.doAction(fact);
                if (r.isExclusive()) {
                    break;
                }
            }
        }

        return anyRuleFired;
    }
}
