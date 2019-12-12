package com.active.services.cart.domain;

import lombok.Data;

@Data
public class CartItemCartItemFee extends BaseDomainObject {

    private Long cartItemId;

    private Long cartItemFeeId;
}