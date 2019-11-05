package com.active.services.cart.domain.discount.algorithm;

import com.active.services.cart.domain.discount.Discount;
import com.active.services.cart.domain.discount.DiscountUtil;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static java.util.Comparator.comparing;

@Component
public class BestDiscountAlgorithm implements DiscountAlgorithm {
    @Override
    public List<Discount> apply(List<Discount> discounts, BigDecimal amountToDiscount, Currency currency) {
        return Collections.singletonList(Collections.max(discounts,
                comparing(disc ->
                        DiscountUtil.getFlatAmount(amountToDiscount, disc.getAmount(), disc.getAmountType(), currency))));
    }
}
