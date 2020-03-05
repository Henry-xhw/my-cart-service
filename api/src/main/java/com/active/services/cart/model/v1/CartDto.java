package com.active.services.cart.model.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class CartDto extends BaseDto {

    @NotNull
    private UUID ownerId;

    @NotNull
    private UUID keyerId;

    @NotNull
    private String currencyCode;

    private UUID reservationGroupId;

    private Set<String> couponCodes;

    @Valid
    private List<CartItemDto> items = new ArrayList<>();
}