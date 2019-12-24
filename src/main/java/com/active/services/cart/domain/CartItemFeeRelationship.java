package com.active.services.cart.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
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