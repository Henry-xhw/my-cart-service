package com.active.services.cart.domain.rule.product;

import java.util.UUID;

import com.active.services.cart.domain.rule.Condition;
import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.Rule;
import com.active.services.product.Fee;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductPriceRule implements Rule {
    @Getter private String identifier = UUID.randomUUID().toString();
    @Getter @Setter private String name;
    @Getter @Setter private int priority;
    @Getter private boolean exclusive;
    private Condition condition;
    private Fee action;

    public ProductPriceRule given(@NonNull Condition condition) {
        this.condition = condition;
        return this;
    }

    public ProductPriceRule then(Fee action) {
        this.action = action;
        return this;
    }

    @Override
    public boolean evaluate(Fact fact) {
        return condition.satisfy(fact);
    }

    @Override
    public void doAction(Fact fact) {
        LOG.info("{} fired, final fee: {}", name, action.getAmount());
    }

    public String ruleExpression() {
        return getClass().getName() + "(identifier == '" + identifier + "', evaluate($fact))";
    }
}
