package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;

import java.util.Optional;

public abstract class CartItemDiscountBasePricer implements CartItemPricer {
    @Override
    public final void quote(CartQuoteContext context, CartItem cartItem) {
        Optional<CartItemFee> priceFeeItems = cartItem.getPriceCartItemFee();
        if (!priceFeeItems.isPresent()) {
            return;
        }

        if (cartItem.isNetPriceNotZero()) {
            doQuote(context, cartItem);
        }
    }

    protected abstract void doQuote(CartQuoteContext context, CartItem cartItem);
}
