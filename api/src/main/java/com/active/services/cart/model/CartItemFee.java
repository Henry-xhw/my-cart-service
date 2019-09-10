package com.active.services.cart.model;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CartItemFee {
    private Long id;
    private String name;
    private String description;
    private CartItemFeeType feeType;
    private FeeTransactionType transactionType;
    private BigDecimal unitPrice;
    private Integer units;
    private BigDecimal subtotal;

    private List<CartItemFee> derivedFees;
}
