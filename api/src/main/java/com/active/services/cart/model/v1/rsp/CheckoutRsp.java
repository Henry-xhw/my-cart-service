package com.active.services.cart.model.v1.rsp;

import java.util.List;

import com.active.services.cart.model.v1.CheckoutResult;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckoutRsp {
    private List<CheckoutResult> checkoutResults;
}
