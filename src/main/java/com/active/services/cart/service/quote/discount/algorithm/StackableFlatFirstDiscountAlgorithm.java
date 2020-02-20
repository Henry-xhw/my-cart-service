package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.service.quote.discount.domain.Discount;
import com.active.services.product.AmountType;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Firstly, sort the discounts by {@link AmountType}: fixed-amount discounts first, flat (amount) discounts second,
 * percentage discounts last.<br>
 * Secondly, sort the discounts with the same {@link AmountType} by amount from lowest to highest.
 * <p>
 * For example, given the following 3 discounts and an amount to discount of $120.00: <br>
 * discount1 = 5% <br>
 * discount2 = $10 <br>
 * discount3 = 10% <br>
 * discount4 = $100 fixed amount <br>
 * The calculation would go as follows: <br>
 * discount4: discount amount = 120 - $100 = $20, price after discount4 = $100 <br>
 * discount2: discount amount = $10, price after discount2 = $100 - $10 = $90 <br>
 * discount1: discount amount = $90 x 5% = $4.50, price after discount1 = $90 - $4.50 = $85.50 <br>
 * discount3: discount amount = $85.50 x 10% = $8.55, price after discount3 = $76.95 <br>
 * Therefore the total discount would be: $20 + $10 + $4.50 + $8.55 = $43.05 = $120 - $76.95
 * <p>
 */
public class StackableFlatFirstDiscountAlgorithm implements DiscountAlgorithm {
    @Override
    public List<Discount> apply(List<Discount> discounts) {
        Collections.sort(discounts, Comparator.comparing(Discount::getAmountType).reversed().thenComparing(Discount::getAmount));
        return discounts;
    }
}
