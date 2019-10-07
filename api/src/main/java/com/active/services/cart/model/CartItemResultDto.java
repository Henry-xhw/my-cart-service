package com.active.services.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResultDto {
    private UUID identifier;
    private String referenceId;
    private String description;
    private List<CartItemFeeResultDto> cartItemFeeResults;
    private List<CartItemResultDto> cartItemResults;
    private boolean paymentOptionAvailable;
    private BigDecimal itemTotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
