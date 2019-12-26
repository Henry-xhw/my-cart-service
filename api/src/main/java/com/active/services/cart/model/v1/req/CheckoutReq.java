package com.active.services.cart.model.v1.req;

import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.domain.Address;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class CheckoutReq {

    @NotNull
    @Valid
    private PaymentAccount paymentAccount;
    private boolean sendReceipt = true;
    private String orderUrl;
    private Address billingAddress;
    private BillingContact billingContact;

}
