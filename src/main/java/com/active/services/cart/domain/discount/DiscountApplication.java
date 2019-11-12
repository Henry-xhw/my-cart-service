package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.discount.algorithm.DiscountAlgorithm;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class DiscountApplication {
    @NonNull private final List<Discount> discounts;
    @NonNull private final DiscountAlgorithm algorithm;

    public void apply(CartItem it, Currency currency) {
        List<Discount> qualified = discounts.stream()
                .filter(Discount::satisfy)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(qualified)) {
            return;
        }
        algorithm.apply(qualified, it.getPrice(), currency).forEach(disc -> it.applyDiscount(disc, currency));
    }
}
