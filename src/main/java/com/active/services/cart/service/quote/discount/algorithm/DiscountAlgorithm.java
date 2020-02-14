package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.service.quote.discount.Discount;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public interface DiscountAlgorithm {
    List<Discount> apply(List<Discount> discounts, BigDecimal amountToDiscount, Currency currency);
}
