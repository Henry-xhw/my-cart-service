package com.active.services.cart.model.v1.req;

import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.CartHolder;
import com.active.services.cart.model.CartItemFeeAllocation;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.domain.Address;

import lombok.Data;

import java.util.List;

import javax.validation.Valid;

@Data
public class CheckoutReq {

    private PaymentAccount paymentAccount;
    private boolean sendReceipt = true;
    private String orderUrl;
    private Address billingAddress;
    private BillingContact billingContact;
    private CartHolder cartHolder;
    @Valid
    private List<CartItemFeeAllocation> feeAllocations;
}
