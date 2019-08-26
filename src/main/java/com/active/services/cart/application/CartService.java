package com.active.services.cart.application;

import com.active.services.cart.model.CreateCartRequest;
import com.active.services.cart.model.CreateCartResp;

public interface CartService {
    CreateCartResp createCart(CreateCartRequest cart);
}
