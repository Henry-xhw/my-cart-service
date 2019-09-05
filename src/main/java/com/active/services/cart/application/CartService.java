package com.active.services.cart.application;

import com.active.services.cart.model.CartDto;
import com.active.services.cart.model.CreateCartResp;

public interface CartService {
    CreateCartResp createCart(CartDto cart);
}
