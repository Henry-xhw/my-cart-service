package com.active.services.cart.model.v1.req;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class QuoteReq {
    @NotNull
    private UUID cartId;
    private boolean aaMember;
}
