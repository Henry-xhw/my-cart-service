package com.active.services.cart.model.v1.rsp;

import com.active.services.cart.model.ErrorCode;

import lombok.Data;

@Data
public class BaseRsp {
    private boolean success = true;

    private ErrorCode errorCode;

    private Object errorPayload;
}
