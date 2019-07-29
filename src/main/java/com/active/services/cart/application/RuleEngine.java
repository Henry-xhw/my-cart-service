package com.active.services.cart.application;

import java.util.List;

public interface RuleEngine {
    void runRule(List<?> facts);
}
