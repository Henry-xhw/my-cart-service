package com.active.services.cart.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;

@Data
public class CartItemFeeAllocation {
    @NotNull
    private UUID cartItemFeeIdentifier;

    private UUID paymentTxId;

    @NotNull
    private BigDecimal amount;
}
