package com.active.services.cart.service.quote.discount.condition;

import com.active.services.cart.domain.CartItem;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NumberOfPersonGEThanThresholdSpec implements DiscountSpecification {
    private final List<CartItem> items;
    private final int threshold;

    @Override
    public boolean satisfy() {
        return items.stream()
                .map(CartItem::getPersonIdentifier)
                .distinct()
                .count() >= threshold;
    }
}
