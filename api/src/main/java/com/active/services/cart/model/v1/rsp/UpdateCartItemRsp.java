package com.active.services.cart.model.v1.rsp;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UpdateCartItemRsp {
    UUID cartId;
}
