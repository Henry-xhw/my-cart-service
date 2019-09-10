package com.active.services.cart.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Data
public class CartDto {
    // a specific string to mark the cart
    private String identifier;
    private String currency;
    @Valid
    @NotEmpty
    private List<CartItemDto> cartItemDtos;
    // a organization identifier likes agencyId
    private String orgIdentifier;
    // a price date of the cart
    private LocalDateTime priceDate;
}
