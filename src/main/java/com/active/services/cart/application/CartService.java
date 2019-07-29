package com.active.services.cart.application;

import com.active.services.cart.model.CartItem;
import com.active.services.cart.model.CreateCartRequest;

import java.util.List;

public interface CartService {
    CreateCartRequest createCart(List<CartItem> items);
}
