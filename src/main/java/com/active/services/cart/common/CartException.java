package com.active.services.cart.common;

import com.active.services.cart.model.ErrorCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartException extends RuntimeException {

    private ErrorCode errorCode;

    private Object payload;

    public CartException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public CartException(ErrorCode errorCode, Object payload) {
        this.errorCode = errorCode;
        this.payload = payload;
    }
}