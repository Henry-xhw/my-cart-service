package com.active.services.cart.application;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;

import java.util.List;

//TODO: can be converted to function to be delayed executed
public interface CartItemSelector {
    List<CartItem> select(Cart cart);
}
