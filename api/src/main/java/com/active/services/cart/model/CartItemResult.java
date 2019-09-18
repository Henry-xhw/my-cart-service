package com.active.services.cart.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResult {

    private String identifier;
    private List<CartItemFeeResult> cartItemFeeResults;
    private List<CartItemResult> cartItemResults;
    private boolean paymentOptionAvailable;
    private BigDecimal itemTotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
