package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.DiscountAmountCalcUtil;
import com.active.services.cart.service.quote.discount.domain.Discount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static java.util.Comparator.comparing;

@RequiredArgsConstructor
public class BestDiscountAlgorithm implements DiscountAlgorithm {
    @NonNull
    private final CartItem cartItem;

    @NonNull
    private Currency currency;

    @Override
    public List<Discount> apply(List<Discount> discounts) {
        return Collections.singletonList(Collections.max(discounts,
                comparing(disc -> DiscountAmountCalcUtil.calcFlatAmount(cartItem.getNetPrice(),
                        disc.getAmount(), disc.getAmountType(), currency))));
    }
}
