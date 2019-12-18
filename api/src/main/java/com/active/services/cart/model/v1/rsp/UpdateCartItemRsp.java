package com.active.services.cart.model.v1.rsp;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateCartItemRsp extends BaseRsp {
    UUID cartId;
}
