package com.active.services.cart.domain;

import com.active.services.cart.model.CurrencyCode;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CartDataFactory {

    public static Cart cart() {
        Cart cart = new Cart();

        cart.setCurrencyCode(CurrencyCode.USD);
        cart.setKeyerId(UUID.randomUUID());
        cart.setOwnerId(UUID.randomUUID());
        cart.setIdentifier(UUID.randomUUID());
        cart.setItems(cartItems());

        return cart;
    }

    private static List<CartItem> cartItems() {
        return Arrays.asList();
    }

    public static CartItem cartItem() {
        CartItem cartItem = new CartItem();


        return cartItem;
    }
}
