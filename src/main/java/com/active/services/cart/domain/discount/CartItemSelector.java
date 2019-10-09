package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;

import java.util.List;

public interface CartItemSelector {
    List<CartItem> select(Cart cart);
}
