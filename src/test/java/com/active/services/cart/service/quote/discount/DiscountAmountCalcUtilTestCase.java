package com.active.services.cart.service.quote.discount;

import com.active.services.product.AmountType;

import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

public class DiscountAmountCalcUtilTestCase {

    @Test
    public void calcFlatAmount() {
        assertEquals(new BigDecimal("24.00"),
                DiscountAmountCalcUtil.calcFlatAmount(new BigDecimal(120),
                        new BigDecimal(20),
                        AmountType.PERCENT,
                        java.util.Currency.getInstance("USD")));

        assertEquals(new BigDecimal(0),
                DiscountAmountCalcUtil.calcFlatAmount(new BigDecimal(120),
                        new BigDecimal(0),
                        AmountType.PERCENT,
                        java.util.Currency.getInstance("USD")));

        assertEquals(new BigDecimal("20.00"),
                DiscountAmountCalcUtil.calcFlatAmount(new BigDecimal(120),
                        new BigDecimal(20),
                        AmountType.FLAT,
                        java.util.Currency.getInstance("USD")));

        assertEquals(new BigDecimal("100.00"),
                DiscountAmountCalcUtil.calcFlatAmount(new BigDecimal(120),
                        new BigDecimal(20),
                        AmountType.FIXED_AMOUNT,
                        java.util.Currency.getInstance("USD")));

        assertEquals(new BigDecimal(0),
                DiscountAmountCalcUtil.calcFlatAmount(new BigDecimal(0),
                        new BigDecimal(20),
                        AmountType.FIXED_AMOUNT,
                        java.util.Currency.getInstance("USD")));


        assertEquals(new BigDecimal("0.00"),
                DiscountAmountCalcUtil.calcFlatAmount(new BigDecimal(20),
                        new BigDecimal(20),
                        AmountType.FIXED_AMOUNT,
                        java.util.Currency.getInstance("USD")));
    }
}
