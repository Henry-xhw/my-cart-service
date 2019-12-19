package com.active.services.cart.model.v1.rsp;

import java.util.UUID;

import lombok.Data;

@Data
public class DeleteCartItemRsp extends BaseRsp {
    private UUID cartId;
}
