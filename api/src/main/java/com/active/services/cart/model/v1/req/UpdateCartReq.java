package com.active.services.cart.model.v1.req;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.Size;

@Data
public class UpdateCartReq {

    private UUID reservationGroupId;

    private Set<String> couponCodes;

    @Size(max = 255)
    private String salesChannel;
}
