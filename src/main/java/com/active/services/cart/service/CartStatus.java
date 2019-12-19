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
    FINALIZED;


    /**
     * Indicates whether the given status is considered complete. (i.e. must be FINALIZED)
     */
    public static boolean isFinalized(CartStatus status) {
        return FINALIZED == status;
    }
    
}
