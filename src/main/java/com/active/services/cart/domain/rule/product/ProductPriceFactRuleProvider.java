package com.active.services.cart.domain.rule.product;

import java.util.Iterator;

import org.drools.template.DataProvider;

import com.active.services.cart.domain.rule.Rule;

public class ProductPriceFactRuleProvider implements DataProvider {
    private Class<?> factClazz;
    private Iterator<Rule> rules;

    public ProductPriceFactRuleProvider(Class<?> factClazz, Iterator<Rule> rules) {
        this.factClazz = factClazz;
        this.rules = rules;
    }

    @Override
    public boolean hasNext() {
        return rules.hasNext();
    }

    @Override
    public String[] next() {
        Rule rule = rules.next();
        return new String[]{factClazz.getName(),
                rule.getName(),
                String.valueOf(rule.getPriority()),
                rule.ruleExpression()};
    }
}
