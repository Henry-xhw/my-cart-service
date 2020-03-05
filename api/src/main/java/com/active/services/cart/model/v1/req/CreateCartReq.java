package com.active.services.cart.model.v1.req;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class CreateCartReq {

    @NotNull
    private UUID ownerId;

    @NotNull
    private UUID keyerId;

    @NotNull
    private String currencyCode;

    private Set<String> couponCodes;

    private UUID reservationId;

    @Size(max = 255)
    private String salesChannel;
}