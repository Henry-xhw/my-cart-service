package com.active.services.cart.application;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.model.CartDto;

public interface CartService {
    Cart createCart(CartDto cartDto);
}
