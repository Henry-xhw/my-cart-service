package com.active.services.cart.service;

public enum CartStatus {
    /**
     * This value indicates the cart is created
     */
    CREATED,

    /**
     * This value indicates the cart is completed the order commit process successfully.
     * once cart status is FINALIZED, cart should never be modify
     */
    FINALIZED
}
