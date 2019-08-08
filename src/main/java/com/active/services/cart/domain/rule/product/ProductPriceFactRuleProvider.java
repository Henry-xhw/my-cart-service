package com.active.services.cart.domain.rule.product;

import java.util.Iterator;

import org.drools.template.DataProvider;

public class ProductPriceFactRuleProvider implements DataProvider {
    private Class<?> factClazz;
    private Iterator<ProductPriceRule> rules;

    public ProductPriceFactRuleProvider(Class<?> factClazz, Iterator<ProductPriceRule> rules) {
        this.factClazz = factClazz;
        this.rules = rules;
    }

    @Override
    public boolean hasNext() {
        return rules.hasNext();
    }

    @Override
    public String[] next() {
        ProductPriceRule rule = rules.next();
        return new String[]{factClazz.getName(),
                rule.getName(),
                String.valueOf(rule.getPriority()),
                rule.ruleExpression()};
    }
}
