package com.active.services.cart.model.v1.rsp;

import java.util.List;
import java.util.UUID;

import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.PaymentAccountResult;
import com.active.services.cart.model.v1.CartItemDto;
import com.active.services.domain.Address;

import lombok.Data;

@Data
public class CheckoutRsp {

    private UUID orderNumber;
    private Long orderId;
    private List<CartItemDto> cartItems;
    private Address billingAddress;
    private BillingContact billingContact;
    private Long agencyId;
    private PaymentAccountResult accountResult;


}
