package com.active.services.common.exception;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CartItemException extends RuntimeException {

    private int errorCode;

    private String message;

    private UUID cartId;

    public CartItemException(int errorCode, String message, UUID cartId) {
        this.message = message;
        this.errorCode = errorCode;
        this.cartId = cartId;
    }
}