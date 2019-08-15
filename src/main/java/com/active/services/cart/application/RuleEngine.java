package com.active.services.cart.application;

import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.Rule;

import java.util.List;

public interface RuleEngine {
    boolean runRules(List<Rule> rules, Fact fact);
}
