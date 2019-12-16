package com.active.services.cart.common;

import static com.active.services.cart.model.ErrorCode.VALIDATION_ERROR;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.active.services.cart.model.ErrorItem;
import com.active.services.cart.model.v1.rsp.BaseRsp;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseRsp methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ErrorItem> errors = exception.getBindingResult().getFieldErrors().stream()
            .map(item -> {
                ErrorItem errorItem = new ErrorItem();
                errorItem.setIdentifier(item.getField());
                errorItem.setMsg(item.getDefaultMessage());

                return errorItem;
            }).collect(Collectors.toList());

        BaseRsp rsp = new BaseRsp();
        rsp.setSuccess(false);
        rsp.setErrorCode(VALIDATION_ERROR);
        rsp.setErrorPayload(errors);

        return rsp;
    }

    @ExceptionHandler({CartException.class})
    public BaseRsp cartExceptionHandler(CartException e) {
        BaseRsp rsp = new BaseRsp();
        rsp.setSuccess(false);
        rsp.setErrorCode(e.getErrorCode());
        rsp.setErrorPayload(e.getPayload());

        return rsp;
    }
}

