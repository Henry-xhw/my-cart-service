package com.active.services.cart.domain;

import java.util.UUID;

import lombok.Data;

@Data
public class CartItemCartItemFee extends BaseDomainObject {

    private Long cartItemId;

    private Long cartItemFeeId;

    public static CartItemCartItemFee buildCartItemCartItemFee(Long cartItemId, Long cartItemFeeId) {
        CartItemCartItemFee cartItemCartItemFee = new CartItemCartItemFee();
        cartItemCartItemFee.setCartItemFeeId(cartItemFeeId);
        cartItemCartItemFee.setCartItemId(cartItemId);
        cartItemCartItemFee.setIdentifier(UUID.randomUUID());
        return cartItemCartItemFee;
    }
}