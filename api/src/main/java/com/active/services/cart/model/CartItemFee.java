package com.active.services.cart.model;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
public class CartItemFee {

    private Long id;
    private String name;
    private CartItemFeeType feeType;
    private FeeTransactionType transactionType;
    private BigDecimal unitPrice;
    private Integer units;
    private BigDecimal subtotal;

    private List<CartItemFee> derivedFees;
}
