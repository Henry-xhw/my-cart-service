package com.active.services.cart.application;

import com.active.services.cart.model.CreateCartRequest;

public interface CartService {
    CreateCartRequest createCart(CreateCartRequest cart);
}
