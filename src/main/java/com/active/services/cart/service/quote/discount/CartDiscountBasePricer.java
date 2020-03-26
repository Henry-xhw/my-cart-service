package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;

import java.util.List;

import static java.util.stream.Collectors.toList;

public abstract class CartDiscountBasePricer implements CartPricer {
    @Override
    public final void quote(CartQuoteContext context) {
        List<CartItem> noneZeroItems = context.getFlattenCartItems().stream()
                .filter(CartItem::isNetPriceNotZero).collect(toList());

        if (!noneZeroItems.isEmpty()) {
            doQuote(context, noneZeroItems);
        }
    }

    protected abstract void doQuote(CartQuoteContext context, List<CartItem> noneZeroItems);
}
