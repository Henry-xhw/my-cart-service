package com.active.services.cart.model.v1.req;

import lombok.Data;

import java.util.List;
import java.util.UUID;

import javax.validation.constraints.NotNull;

@Data
public class CreateCartReq {

    @NotNull
    private UUID ownerId;

    @NotNull
    private UUID keyerId;

    @NotNull
    private String currencyCode;

    private List<String> couponCodes;
}