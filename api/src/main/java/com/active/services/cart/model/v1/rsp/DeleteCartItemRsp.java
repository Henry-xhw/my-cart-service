package com.active.services.cart.model.v1.rsp;

import lombok.Data;

import java.util.UUID;

@Data
public class DeleteCartItemRsp {
    private UUID cartId;
}
