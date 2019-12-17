package com.active.services.cart.service.quote;

import com.active.services.cart.domain.Cart;

import lombok.Data;

@Data
public class CartQuoteContext {
    private Cart cart;

    public CartQuoteContext(Cart cart) {
        this.cart = cart;
    }
}
