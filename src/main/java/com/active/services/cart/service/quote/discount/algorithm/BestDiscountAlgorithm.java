package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.DiscountAmountCalcUtil;
import com.active.services.cart.service.quote.discount.DiscountApplication;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Currency;
import java.util.List;

import static java.util.Comparator.comparing;

/**
 * Using the given discounts, determines which individual one provides the biggest savings to the consumer.
 * <p>
 * For example, given the following 3 discounts and an amount to discount of $80.00: <br>
 * discount 1 = $20 FLAT <br>
 * discount 2 = 20% PERCENT <br>
 * discount 3 = $70 FIXED_AMOUNT <br>
 * Discount #1 provides the biggest savings to the consumer ($20, versus $16 and $10): <br>
 * discount 1 = $20 <br>
 * discount 2 = 20% x $80 = $16 <br>
 * discount 3 = 80-70 = $10 <br>
 * <p>
 */
@RequiredArgsConstructor
public class BestDiscountAlgorithm implements DiscountAlgorithm {
    @NonNull
    private final CartItem cartItem;

    @NonNull
    private Currency currency;

    @Override
    public List<DiscountApplication> apply(List<DiscountApplication> discounts) {
        return Collections.singletonList(Collections.max(discounts,
                comparing(disc -> DiscountAmountCalcUtil.calcFlatAmount(cartItem.getNetPrice(),
                        disc.getAmount(), disc.getAmountType(), currency))));
    }
}
