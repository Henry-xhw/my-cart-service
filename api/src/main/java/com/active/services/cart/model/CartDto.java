package com.active.services.cart.model;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

public class CartDto {
    private String currency;
    @Valid
    @NotEmpty
    private List<CartItem> cartItems;
    private BigDecimal subtotal;
}
