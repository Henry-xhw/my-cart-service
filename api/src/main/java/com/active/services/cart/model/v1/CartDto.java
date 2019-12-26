package com.active.services.cart.model.v1;

import com.active.services.cart.model.CurrencyCode;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class CartDto extends BaseDto {

    @NotNull
    private UUID ownerId;

    @NotNull
    private UUID keyerId;

    @NotNull
    private CurrencyCode currencyCode;

    @Valid
    private List<CartItemDto> items = new ArrayList<>();
}
