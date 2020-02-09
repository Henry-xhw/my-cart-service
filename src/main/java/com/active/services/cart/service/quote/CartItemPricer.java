package com.active.services.cart.service.quote;

import com.active.services.cart.domain.CartItem;

public interface CartItemPricer {
    void quote(CartQuoteContext context, CartItem cartItem);
}