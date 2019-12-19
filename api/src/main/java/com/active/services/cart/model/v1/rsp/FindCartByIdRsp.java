package com.active.services.cart.model.v1.rsp;

import javax.validation.Valid;

import com.active.services.cart.model.v1.CartDto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCartByIdRsp extends BaseRsp {
    @Valid
    private CartDto cart;
}
