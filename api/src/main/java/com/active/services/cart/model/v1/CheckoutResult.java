package com.active.services.cart.model.v1;

<<<<<<< HEAD
=======
import com.active.services.cart.model.BillingContact;
>>>>>>> develop
import com.active.services.cart.model.PaymentAccountResult;

import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutResult {
    private Long orderId;
    private PaymentAccountResult accountResult;
}
