package com.active.services.cart.model.v1.req;

import java.util.UUID;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class QuoteReq {
    @NotNull
    private UUID cartId;
}
