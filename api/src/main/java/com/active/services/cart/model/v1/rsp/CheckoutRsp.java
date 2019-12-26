package com.active.services.cart.model.v1.rsp;

import com.active.services.cart.model.v1.CheckoutResult;

import lombok.Data;

import java.util.List;

@Data
public class CheckoutRsp {
    private List<CheckoutResult> checkoutResults;
}
