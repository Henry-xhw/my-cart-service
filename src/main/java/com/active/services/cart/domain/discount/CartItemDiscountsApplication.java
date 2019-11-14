package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.discount.algorithm.DiscountAlgorithm;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class CartItemDiscountsApplication {
    @NonNull private final CartItem item;
    @NonNull private final List<Discount> discounts;
    @NonNull private final DiscountAlgorithm algorithm;
    @NonNull private final Currency currency;

    public void apply() {
        if (item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        List<Discount> qualified = discounts.stream()
                .filter(Discount::satisfy)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(qualified)) {
            return;
        }
        algorithm.apply(qualified, item.getPrice(), currency).forEach(disc -> item.applyDiscount(disc, currency));
    }
}
