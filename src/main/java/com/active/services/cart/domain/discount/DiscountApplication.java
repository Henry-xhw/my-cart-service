package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.discount.algorithm.DiscountAlgorithm;

import lombok.Data;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DiscountApplication {
    private List<CartItem> candidates;
    private List<Discount> discounts;

    public void apply(CartItem it, DiscountAlgorithm algorithm, Currency currency) {
        List<Discount> qualified = discounts.stream()
                .filter(Discount::satisfy)
                .collect(Collectors.toList());

        algorithm.apply(qualified, it.getTotal(), currency).forEach(it::applyDiscount);
    }
}
