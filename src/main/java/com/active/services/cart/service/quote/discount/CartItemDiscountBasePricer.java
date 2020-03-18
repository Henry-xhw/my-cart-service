package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;

public abstract class CartItemDiscountBasePricer implements CartItemPricer {
    @Override
    public final void quote(CartQuoteContext context, CartItem cartItem) {
        if (cartItem.isNetPriceNotZero()) {
            doQuote(context, cartItem);
        }
    }

    protected abstract void doQuote(CartQuoteContext context, CartItem cartItem);
}
