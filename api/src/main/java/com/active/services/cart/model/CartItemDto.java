package com.active.services.cart.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CartItemDto {
    private Long id;
    private Long productId;
    private int quantity;
    private CartItemOption option;
    private BigDecimal priceOverride;
    private CartItemFacts cartItemFacts;
    //?private Long agencyId;

    private List<CartItemFee> fees;
    private BigDecimal itemTotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;

    @Builder
    public CartItemDto(Long productId, int quantity, CartItemOption option, BigDecimal overridePrice, CartItemFacts facts) {
        this.productId = productId;
        this.quantity = quantity;
        this.option = option;
        this.priceOverride = overridePrice;
        this.cartItemFacts = facts;
    }
}
