package com.active.services.cart.service.checkout;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.CartHolder;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.domain.Address;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CheckoutContext {
    private PaymentAccount paymentAccount;

    private boolean sendReceipt = true;

    private String orderUrl;

    private Address billingAddress;

    private BillingContact billingContact;

    private CartHolder cartHolder;

    private Cart cart;

    private List<CheckoutResult> checkoutResults = new ArrayList<>();
}
