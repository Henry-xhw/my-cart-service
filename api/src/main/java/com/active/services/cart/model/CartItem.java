package com.active.services.cart.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartItem {
    private Long productId;
    private int quantity;
    private CartItemOption option;
    private BigDecimal priceOverride;
    private CartItemFacts cartItemFacts;

    private List<CartItemFee> fees;
    private BigDecimal itemTotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;

    @Builder
    public CartItem(Long productId, int quantity, CartItemOption option, BigDecimal overridePrice, CartItemFacts facts) {
        this.productId = productId;
        this.quantity = quantity;
        this.option = option;
        this.priceOverride = overridePrice;
        this.cartItemFacts = facts;
    }
}
