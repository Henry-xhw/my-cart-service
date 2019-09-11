package com.active.services.cart.application;

import java.util.List;

import com.active.services.cart.model.CartDto;
import com.active.services.cart.model.CartResult;

public interface CartService {
    List<CartResult> createCarts(List<CartDto> carts);
}
