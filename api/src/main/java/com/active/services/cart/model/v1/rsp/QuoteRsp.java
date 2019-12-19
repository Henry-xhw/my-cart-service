package com.active.services.cart.model.v1.rsp;

import com.active.services.cart.model.v1.QuoteCartDto;
import lombok.Data;

@Data
public class QuoteRsp {
    private QuoteCartDto cartDto;
}
