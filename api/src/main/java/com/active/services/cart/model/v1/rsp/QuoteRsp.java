package com.active.services.cart.model.v1.rsp;

import com.active.services.cart.model.v1.CartDto;

import lombok.Data;

@Data
public class QuoteRsp {
    private CartDto cartDto;
}
