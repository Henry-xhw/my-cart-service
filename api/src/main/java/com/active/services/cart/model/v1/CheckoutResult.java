package com.active.services.cart.model.v1;

import com.active.services.cart.model.PaymentAccountResult;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutResult {
    private Long orderId;
    private PaymentAccountResult paymentResult;
}
