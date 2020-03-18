package com.active.services.cart.service.quote.discount.condition;

@FunctionalInterface
public interface DiscountSpecification {
    boolean satisfy();
}
