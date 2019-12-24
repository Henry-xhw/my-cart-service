package com.active.services.cart.common;

import com.active.services.cart.model.ErrorCode;

import lombok.Getter;
import lombok.Setter;

import java.text.MessageFormat;

@Getter
@Setter
public class CartException extends RuntimeException {

    private ErrorCode errorCode;
    private String errorMessage;

    public CartException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public CartException(ErrorCode errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public CartException(ErrorCode errorCode, String message, Object... args) {
        this(errorCode, MessageFormat.format(message, args));
    }
}