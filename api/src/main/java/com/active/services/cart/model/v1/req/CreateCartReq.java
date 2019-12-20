package com.active.services.cart.model.v1.req;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.active.services.cart.model.CurrencyCode;

import lombok.Data;

@Data
public class CreateCartReq {

    @NotNull
    private UUID ownerId;

    @NotNull
    private UUID keyerId;

    @NotNull
    private CurrencyCode currencyCode;
}
