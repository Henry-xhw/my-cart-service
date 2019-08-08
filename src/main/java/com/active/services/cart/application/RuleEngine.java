package com.active.services.cart.application;

import java.util.List;

import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.Rule;

public interface RuleEngine {
    void runRule(List<?> facts);

    void runRules(List<Rule> rules, Fact fact);
}
