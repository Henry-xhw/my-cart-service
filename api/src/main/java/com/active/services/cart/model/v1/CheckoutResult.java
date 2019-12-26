package com.active.services.cart.model.v1;

import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.PaymentAccountResult;
import com.active.services.domain.Address;

import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutResult {
    private UUID orderNumber;
    private Long orderId;
    private Address billingAddress;
    private BillingContact billingContact;
    private Long agencyId;
    private PaymentAccountResult accountResult;
}
