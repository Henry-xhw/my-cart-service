package com.active.services.cart.common;

import lombok.Getter;

public enum OperationResultCode {
    CART_NOT_EXIST(4001, "cart not exist."),
    CART_ITEM_NOT_EXIST(4002, "cart item not exist."),
    INVALID_PARAMETER(5001,"invalid parameters.");

    @Getter
    private final int code;
    @Getter
    private final String description;

    OperationResultCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}