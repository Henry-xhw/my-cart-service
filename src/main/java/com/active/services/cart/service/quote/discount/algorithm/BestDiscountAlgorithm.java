package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.cart.service.quote.discount.DiscountAmountCalcUtil;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static java.util.Comparator.comparing;

public class BestDiscountAlgorithm implements DiscountAlgorithm {
    @Override
    public List<Discount> apply(List<Discount> discounts, BigDecimal amountToDiscount, Currency currency) {
        return Collections.singletonList(Collections.max(discounts,
                comparing(disc -> DiscountAmountCalcUtil.calcFlatAmount(amountToDiscount,
                        disc.getAmount(), disc.getAmountType(), currency))));
    }
}
