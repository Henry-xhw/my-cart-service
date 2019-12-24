package com.active.services.cart.domain;

import lombok.Data;

import java.util.List;

@Data
public class MultiCartItemFee {

    private CartItemFee fee;

    private List<CartItem> cartItems;
}
