package com.active.services.cart.model.v1;

import java.util.UUID;

import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.PaymentAccountResult;
import com.active.services.domain.Address;

import lombok.Data;

@Data
public class CheckoutResult {
    private UUID orderNumber;
    private Long orderId;
    private Address billingAddress;
    private BillingContact billingContact;
    private Long agencyId;
    private PaymentAccountResult accountResult;

    public CheckoutResult(Long orderId) {
        this.orderId = orderId;
    }
}
