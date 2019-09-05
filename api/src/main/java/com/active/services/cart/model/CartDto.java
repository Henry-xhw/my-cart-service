package com.active.services.cart.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
public class CartDto {
    private Long id;
    private String identifier;
    private String currency;
    @Valid
    @NotEmpty
    private List<CartItemDto> cartItemDtos;
    //?private Long agencyId;
    //?private String orgIdentifier;

    private BigDecimal subtotal;
    private BigDecimal feeTotal;
    private BigDecimal taxTotal;
}
