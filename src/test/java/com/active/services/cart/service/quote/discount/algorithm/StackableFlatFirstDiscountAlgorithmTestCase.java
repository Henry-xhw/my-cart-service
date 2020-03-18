package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.DiscountFactory;
import com.active.services.product.AmountType;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class StackableFlatFirstDiscountAlgorithmTestCase {

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
     */
    @Test
    public void apply() {

        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        Discount discountApplication1 = DiscountFactory.getCouponCodeDiscountApplication(
                AmountType.PERCENT, new BigDecimal("5.00"), "code", null, null, null, cartQuoteContext);

        Discount discountApplication2 = DiscountFactory.getCouponCodeDiscountApplication(
                AmountType.FLAT, new BigDecimal("10.00"), "code", null, null, null, cartQuoteContext);

        Discount discountApplication3 = DiscountFactory.getCouponCodeDiscountApplication(
                AmountType.PERCENT, new BigDecimal("10.00"), "code", null, null, null, cartQuoteContext);

        Discount discountApplication4 = DiscountFactory.getCouponCodeDiscountApplication(
                AmountType.FIXED_AMOUNT, new BigDecimal("100.00"), "code", null, null, null, cartQuoteContext);

        StackableFlatFirstDiscountAlgorithm discountAlgorithm = new StackableFlatFirstDiscountAlgorithm();
        List<Discount> results = discountAlgorithm.apply(Arrays.asList(discountApplication1,
                discountApplication2, discountApplication3, discountApplication4));
        assertEquals(4, results.size());
        assertEquals(discountApplication4, results.get(0));
        assertEquals(discountApplication2, results.get(1));
        assertEquals(discountApplication1, results.get(2));
        assertEquals(discountApplication3, results.get(3));
    }
}
