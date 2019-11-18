package com.active.services.cart.model.v1.req;

import com.active.services.cart.model.v1.CartDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CreateCartReq {
    @NotNull
    private CartDto cart;
}
