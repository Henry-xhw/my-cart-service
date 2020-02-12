package com.active.services.cart.domain.discount.condition;


import com.active.services.cart.domain.CartItem;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class NumberOfProdGEThanThresholdSpec implements DiscountSpecification {
    private final List<CartItem> items;
    private final int threshold;

    @Override
    public boolean satisfy() {
        return items.stream()
                .mapToInt(CartItem::getQuantity)
                .sum() >= threshold;
    }
}
