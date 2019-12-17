package com.active.services.cart.model.v1.rsp;

import java.time.Instant;

import com.active.services.cart.model.ErrorCode;

import lombok.Data;

@Data
public class BaseRsp {
    private boolean success = true;

    private ErrorCode errorCode;

    private Instant timeStamp = Instant.now();

    private String errorMessage;
}
