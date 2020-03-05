package com.active.services.cart.model.v1.req;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class UpdateCartReq {

    private Set<String> couponCodes;

    private UUID reservationGroupId;
}
