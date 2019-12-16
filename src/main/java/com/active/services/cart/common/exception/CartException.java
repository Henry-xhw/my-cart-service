package com.active.services.cart.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartException extends RuntimeException {

    private HttpStatus httpStatus;

    private Object payload;

    public CartException(HttpStatus httpStatus) {
        this(httpStatus, null);
    }

    public CartException(HttpStatus httpStatus, Object payload) {
        this.httpStatus = httpStatus;
        this.payload = payload;
    }
}