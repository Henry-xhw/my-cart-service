package com.active.services.cart.service.checkout;

import com.active.services.cart.common.Event;

public class CheckoutEvent extends Event {
    public enum CheckoutPhase {
        COMMIT_PAYMENT,
        COMMIT_INVENTORY,
        COMMIT_ORDER;
    }
}
