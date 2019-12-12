package com.active.services.cart.model.v1.req;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.domain.Address;

import lombok.Data;

@Data
public class CheckoutReq {
    @NotNull
    private UUID cartId;

    @NotNull
    @Valid
    private PaymentAccount paymentAccount;
    private Address billingAddress;
    private BillingContact billingContact;

}
