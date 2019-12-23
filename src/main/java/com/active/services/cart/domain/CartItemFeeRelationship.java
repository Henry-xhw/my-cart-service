package com.active.services.cart.domain;

import java.util.UUID;

import lombok.Data;

@Data
public class CartItemFeeRelationship extends BaseDomainObject {

    private Long cartItemId;

    private Long cartItemFeeId;

    public static CartItemFeeRelationship buildCartItemCartItemFee(Long cartItemId, Long cartItemFeeId) {
        CartItemFeeRelationship cartItemFeeRelationship = new CartItemFeeRelationship();
        cartItemFeeRelationship.setCartItemFeeId(cartItemFeeId);
        cartItemFeeRelationship.setCartItemId(cartItemId);
        cartItemFeeRelationship.setIdentifier(UUID.randomUUID());
        return cartItemFeeRelationship;
    }
}