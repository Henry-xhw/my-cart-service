package com.active.services.cart.model.v1.rsp;

import com.active.services.cart.model.v1.CartDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindCartByIdRsp {
    private CartDto cart;
}
