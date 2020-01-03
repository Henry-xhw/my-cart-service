package com.active.services.cart.model.v1;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class CartDto extends BaseDto {

    @NotNull
    private UUID ownerId;

    @NotNull
    private UUID keyerId;

    @NotNull
    private String currencyCode;

    @Valid
    private List<CartItemDto> items = new ArrayList<>();
}