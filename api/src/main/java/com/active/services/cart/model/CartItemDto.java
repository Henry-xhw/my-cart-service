package com.active.services.cart.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartItemDto {
    // a specific string to mark the cart item.
    private String identifier;
    private Long productId;
    private int quantity;
    private CartItemOption option;
    // for pricing override
    private BigDecimal priceOverride;
    // it will take some dynamical properties
    private CartItemFacts cartItemFacts;
    // indicate parent-child relationships between cartItems
    private String parentIdentifier;

}
