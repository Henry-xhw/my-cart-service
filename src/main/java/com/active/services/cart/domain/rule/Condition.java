package com.active.services.cart.domain.rule;

import com.active.services.cart.domain.rule.Fact;

public interface Condition {
    boolean satisfy(Fact fact);
}
