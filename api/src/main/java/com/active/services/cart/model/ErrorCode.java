package com.active.services.cart.model;

/**
 * Business error code for caller.
 *
 */
public enum ErrorCode {
    INTERNAL_ERROR,
    VALIDATION_ERROR,
    CART_NOT_FOUND,
    CART_ITEM_NOT_FOUND,
    CART_LOCKED,
    CART_PRICING_OUT_OF_DATE,
    OUT_OF_INVENTORY,
}
