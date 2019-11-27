package com.active.services.cart.model.v1.rsp;

import com.active.services.cart.model.v1.CartDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;

@Getter
@Setter
public class FindCartByIdRsp {
    @Valid
    private CartDto cart;
}
