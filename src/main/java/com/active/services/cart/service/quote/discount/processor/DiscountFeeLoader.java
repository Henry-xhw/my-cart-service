package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.Discount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DiscountFeeLoader {

    @NonNull
    private final CartQuoteContext cartQuoteContext;
    @NonNull private final CartItem item;
    @NonNull private final Discount disc;

    public void apply() {
        cartQuoteContext.addAppliedDiscount(disc);
        item.applyDiscount(disc, cartQuoteContext.getCurrency().getCurrencyCode());
    }
}
