package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.discount.algorithm.DiscountAlgorithm;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class DiscountApplication {
    private List<Discount> discounts;

    public void apply(CartItem it, DiscountAlgorithm algorithm, Currency currency) {
        List<Discount> qualified = discounts.stream()
                .filter(Discount::satisfy)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(qualified)) {
            return;
        }
        algorithm.apply(qualified, it.getPrice(), currency).forEach(it::applyDiscount);
    }
}
