package com.active.services.cart.model;

import java.math.BigDecimal;
import java.util.List;

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
    private CartItemOption option;
    private BigDecimal priceOverride;
    private CartItemFacts cartItemFacts;

    private List<CartItemFee> fees;
    private BigDecimal itemTotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
