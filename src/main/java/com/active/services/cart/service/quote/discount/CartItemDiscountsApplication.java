package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;
import com.active.services.cart.service.quote.discount.processor.DiscountFeeLoader;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CartItemDiscountsApplication {

    @NonNull private final CartQuoteContext cartQuoteContext;
    @NonNull private final CartItem item;
    @NonNull private final List<Discount> discounts;
    @NonNull private final DiscountAlgorithm algorithm;

    public void apply() {

        if (item.getNetPrice().compareTo(BigDecimal.ZERO) <= 0) {
            return;
        }
        List<Discount> qualified = discounts.stream()
                .filter(Discount::satisfy)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(qualified)) {
            return;
        }

        algorithm.apply(qualified).forEach(disc -> new DiscountFeeLoader(cartQuoteContext, item, disc).apply());
    }
}
