package com.active.services.cart.model.v1.rsp;

import com.active.services.cart.model.v1.CheckoutResult;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CheckoutRsp extends BaseRsp {
    private List<CheckoutResult> checkoutResults;
}
