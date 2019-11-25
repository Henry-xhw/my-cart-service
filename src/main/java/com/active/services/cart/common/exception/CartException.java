package com.active.services.cart.common.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartException extends RuntimeException {

    private int errorCode;

    private String message;

    public CartException(int errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
    }
}