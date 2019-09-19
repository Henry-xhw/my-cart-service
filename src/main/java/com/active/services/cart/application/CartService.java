package com.active.services.cart.application;

import com.active.services.cart.model.CartDto;
import com.active.services.cart.model.CartResult;

public interface CartService {
    CartResult createCart(CartDto cartDto);
}
