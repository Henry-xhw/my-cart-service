package com.active.services.cart.application;

import java.util.List;

import com.active.services.cart.model.CartDto;
import com.active.services.cart.model.CreateCartsResp;

public interface CartService {
    CreateCartsResp createCarts(List<CartDto> carts);
}
