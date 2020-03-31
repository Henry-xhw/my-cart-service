package com.active.services.cart.service.validator;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;

import org.junit.Test;

import java.util.Arrays;
import java.util.UUID;

public class CartItemsIdentifierValidatorTestCase {
    @Test
    public void validatePass() {
        Cart cart = new Cart();

        CartItem cartItem = new CartItem();
        cartItem.setIdentifier(UUID.randomUUID());
        cart.setItems(Arrays.asList(cartItem));

        new CartItemsIdentifierValidator(cart, Arrays.asList(cartItem)).validate();
    }

    @Test(expected = CartException.class)
    public void validateFailed() {
        Cart cart = new Cart();

        CartItem cartItem = new CartItem();
        cartItem.setIdentifier(UUID.randomUUID());
        CartItem cartItem2 = new CartItem();
        cartItem2.setIdentifier(UUID.randomUUID());

        cart.setItems(Arrays.asList(cartItem));

        new CartItemsIdentifierValidator(cart, Arrays.asList(cartItem, cartItem2)).validate();
    }
}
