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
    PLACE_ORDER_ERROR,
    INVENTORY_COMMIT_ERROR
}
