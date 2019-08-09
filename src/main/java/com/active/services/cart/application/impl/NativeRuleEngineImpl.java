package com.active.services.cart.application.impl;

import java.util.Comparator;
import java.util.List;

import com.active.services.cart.application.RuleEngine;
import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.Rule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NativeRuleEngineImpl implements RuleEngine {

    @Override
    public void runRules(List<Rule> rules, Fact fact) {
        rules.sort(Comparator.comparingInt(Rule::getPriority));

        for (Rule r : rules) {
            boolean toFire = r.evaluate(fact);
            LOG.debug("rule: {}, priority: {}, to fire: {}", r.getName(), r.getPriority(), toFire);
            if (toFire) {
                LOG.info("rule: {}, priority: {} fired, exclusive: {}", r.getName(), r.getPriority(), r.isExclusive());
                r.doAction(fact);
                if (r.isExclusive()) {
                    break;
                }
            }
        }
    }
}
