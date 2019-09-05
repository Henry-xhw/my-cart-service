package com.active.services.cart.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class CartItemFee {
    private String name;
    private BigDecimal unitPrice;
    private Integer units;
    private BigDecimal subtotal;
}
