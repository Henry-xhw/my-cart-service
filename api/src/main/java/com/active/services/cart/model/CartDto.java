package com.active.services.cart.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
public class CartDto {
    private String currency;
    @Valid
    @NotEmpty
    private List<CartItem> cartItems;

    private BigDecimal subtotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
