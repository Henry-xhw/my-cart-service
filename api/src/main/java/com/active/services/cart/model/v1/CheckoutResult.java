package com.active.services.cart.model.v1;

import com.active.services.cart.model.PaymentAccountResult;

import lombok.Data;

@Data
public class CheckoutResult {
    private Long orderId;
    private PaymentAccountResult paymentResult;

    public CheckoutResult(Long orderId, PaymentAccountResult paymentResult) {
        this.orderId = orderId;
        this.paymentResult = paymentResult;
    }
}
