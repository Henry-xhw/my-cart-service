package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.product.AmountType;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

public class StackableFlatFirstDiscountAlgorithm implements DiscountAlgorithm {
    @Override
    public List<Discount> apply(List<Discount> discounts, BigDecimal amountToDiscount, Currency currency) {
        discounts.sort((d1, d2) -> {
            if (d1.getAmountType() == d2.getAmountType()) {
                return 0;
            }
            return d1.getAmountType() == AmountType.FLAT ? -1 : 1;
        });
        return discounts;
    }
}
