package com.active.services.cart.model.v1.rsp;

import com.active.services.cart.model.v1.CheckoutResult;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class CheckoutRsp extends BaseRsp {
    private List<CheckoutResult> checkoutResults;
}
