package com.active.services.cart.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.Data;

@Data
public class CartItemResult {

    private String identifier;
    private Long productId;
    private int quantity;
    private CartItemOption option;
    private BigDecimal priceOverride;
    private CartItemFacts cartItemFacts;
    private String parentIdentifier;
    private List<CartItemFee> cartItemFeeList;
    private BigDecimal itemTotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
