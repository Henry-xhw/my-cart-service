package com.active.services.cart.model.v1.req;

import lombok.Data;

import java.util.UUID;

@Data
public class SearchCartReq {
    private UUID ownerId;
}
