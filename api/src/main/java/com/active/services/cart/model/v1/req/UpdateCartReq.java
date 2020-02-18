package com.active.services.cart.model.v1.req;

import lombok.Data;

import java.util.List;

@Data
public class UpdateCartReq {

    private List<String> couponCodes;
}
