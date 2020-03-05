package com.active.services.cart.model.v1.req;

import lombok.Data;

import java.util.Set;

import javax.validation.constraints.Size;

@Data
public class UpdateCartReq {

    private Set<String> couponCodes;

    @Size(max = 255)
    private String salesChannel;
}
