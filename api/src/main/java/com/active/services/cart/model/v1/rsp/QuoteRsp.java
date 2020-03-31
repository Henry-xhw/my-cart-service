package com.active.services.cart.model.v1.rsp;

import com.active.services.cart.model.v1.QuoteCartDto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class QuoteRsp extends BaseRsp {
    private QuoteCartDto cartDto;
}
