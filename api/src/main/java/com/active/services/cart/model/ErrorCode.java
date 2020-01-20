package com.active.services.cart.model;

/**
 * Business error code for caller.
 *
 */
public enum ErrorCode {
    QUOTE_ERROR(false),
    INTERNAL_ERROR(false),
    VALIDATION_ERROR(false),
    CART_NOT_FOUND(false),
    CART_ITEM_NOT_FOUND(false),
    CART_LOCKED(true),
    CART_PRICING_OUT_OF_DATE(true),
    OUT_OF_INVENTORY(true);

    private final boolean isBizException;

    private ErrorCode(boolean isBizException) {
        this.isBizException = isBizException;
    }
}
