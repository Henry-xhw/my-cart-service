package com.active.services.cart.common.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class CartBizException extends CartException {
    public CartBizException(String bizKey, Object value) {
        this(null);

        Map<String, Object> payload = new HashMap<>();
        payload.put(bizKey, value);
        setPayload(payload);
    }

    public CartBizException(Object payload) {
        super(HttpStatus.OK, payload);
    }

    public CartBizException() {
        this(null);
    }
}
