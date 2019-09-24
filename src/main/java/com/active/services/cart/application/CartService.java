package com.active.services.cart.application;

import com.active.services.cart.model.CartDto;
import com.active.services.cart.model.CartResultDto;

public interface CartService {
    CartResultDto createCart(CartDto cartDto);
}
