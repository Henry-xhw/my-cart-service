package com.active.services.cart.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class CartResult {
    private String identifier;
    private String currency;
    private List<CartItemResult> cartItemResults;
    private String orgIdentifier;
    private LocalDateTime priceDate;
    private BigDecimal subtotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
