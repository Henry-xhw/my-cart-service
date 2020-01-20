package com.active.services.cart.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CartItemFeeAllocation {

    private UUID cartItemFeeIdentifier;

    private BigDecimal amount;
}
