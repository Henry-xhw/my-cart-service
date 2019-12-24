package com.active.services.cart.model.v1.req;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.CartHolder;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.domain.Address;

import lombok.Data;

@Data
public class CheckoutReq {

    @NotNull
    @Valid
    private PaymentAccount paymentAccount;
    private boolean sendReceipt = true;
    private String orderUrl;
    private Address billingAddress;
    private BillingContact billingContact;
    private CartHolder cartHolder;

}
