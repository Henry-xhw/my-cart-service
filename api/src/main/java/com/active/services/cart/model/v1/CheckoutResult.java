package com.active.services.cart.model.v1;

import com.active.services.cart.model.PaymentAccountResult;

import lombok.Data;


@Data
public class CheckoutResult {
    private Long orderId;
    private PaymentAccountResult accountResult;

    public CheckoutResult(Long orderId) {
        this.orderId = orderId;
    }
}
