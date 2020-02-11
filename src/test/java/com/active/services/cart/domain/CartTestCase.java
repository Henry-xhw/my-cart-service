package com.active.services.cart.domain;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

public class CartTestCase {

    @Test
    public void testFindCartItemSuccess(){
        Cart cart = CartDataFactory.cart();
        UUID cartItemId = cart.getItems().get(0).getIdentifier();
        Optional<CartItem> cartItem = cart.findCartItem(cartItemId);
        Assert.assertTrue(cartItem.isPresent());
        Assert.assertEquals(cartItem.get().getIdentifier(), cartItemId);
    }

    @Test
    public void testFindCartItemFail(){
        Cart cart = CartDataFactory.cart();
        UUID cartItemId = cart.getItems().get(0).getIdentifier();
        cart.getItems().set(0, null);
        Optional<CartItem> cartItem = cart.findCartItem(cartItemId);
        Assert.assertFalse(cartItem.isPresent());
    }

}
