package com.active.services.cart.application;


import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;

import java.util.List;

//TODO: can be converted to function to be delayed executed
public interface CartItemSelector {
    List<CartItem> select(Cart cart);
}
