package com.active.services.cart.domain.discount.algorithm;

import com.active.services.cart.domain.discount.Discount;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class ToMostExpensiveItemAlgorithm implements DiscountAlgorithm {
    @Override
    public List<Discount> apply(List<Discount> discounts, BigDecimal amountToDiscount, Currency currency) {
        return null;
    }
}
