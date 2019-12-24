package com.active.services.cart.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CartItemFeesInCart extends CartItemFee {
    private long cartItemId;
}
