package com.active.services.cart.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class CartItem {
    private Long productId;
    private int quantity;
    private BigDecimal price;
    private BigDecimal priceOverride;

    private CartItemFact cartItemFact;
}
