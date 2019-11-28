package com.active.services.cart.common;

import lombok.Getter;

public enum OperationResultCode {
    CART_NOT_EXIST(4001, "cart not exist."),
    CART_ITEM_NOT_EXIST(4002, "cart item not exist.");

    @Getter
    private int code;
    @Getter
    private String description;

    OperationResultCode(int code, String description) {
        this.code = code;
        this.description = description;
    }
}