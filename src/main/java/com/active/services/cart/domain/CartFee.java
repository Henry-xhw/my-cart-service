package com.active.services.cart.domain;

import java.util.List;

import lombok.Data;

@Data
public class CartFee {

    private CartItemFee fee;

    private List<CartItem> cartItems;
}
