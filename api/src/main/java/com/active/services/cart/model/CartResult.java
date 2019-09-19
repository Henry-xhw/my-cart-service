package com.active.services.cart.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CartResult {
    private UUID identifier;
    private String currency;
    private List<CartItemResult> cartItemResults;
    private LocalDateTime priceDate;
    private BigDecimal subtotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
