package com.active.services.cart.model.v1;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;

@Data
public class CartItemFeeAllocationDTO {
    @NotNull
    private UUID cartItemFeeIdentifier;

    @NotNull
    private BigDecimal amount;
}
