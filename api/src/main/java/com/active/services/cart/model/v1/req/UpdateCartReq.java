package com.active.services.cart.model.v1.req;

import lombok.Data;

import java.util.Set;

@Data
public class UpdateCartReq {

    private Set<String> couponCodes;
}
