package com.active.services.cart.common;

import com.active.services.cart.model.v1.rsp.BaseRsp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.active.services.cart.model.ErrorCode.INTERNAL_ERROR;
import static com.active.services.cart.model.ErrorCode.VALIDATION_ERROR;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private static final String ERROR_MSG_TMPL = "%s: %s";

    private final ObjectMapper objectMapper;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public BaseRsp methodArgumentNotValidException(MethodArgumentNotValidException exception)
            throws JsonProcessingException {
        ResponseEntity.BodyBuilder builder = ResponseEntity.badRequest();

        List<String> errors = exception.getBindingResult().getFieldErrors().stream()
            .map(item -> item.getField() + ": " + item.getDefaultMessage()).collect(Collectors.toList());

        String msg = errorMsgWithTrackingId(objectMapper.writeValueAsString(errors));
        BaseRsp rsp = new BaseRsp();
        rsp.setSuccess(false);
        rsp.setErrorCode(VALIDATION_ERROR);
        rsp.setErrorMessage(msg);

        LOG.error(msg);

        return rsp;
    }

    @ExceptionHandler({CartException.class})
    public BaseRsp cartExceptionHandler(CartException e) {
        BaseRsp rsp = new BaseRsp();
        rsp.setSuccess(false);
        rsp.setErrorCode(e.getErrorCode());
        String msg = errorMsgWithTrackingId(e.getErrorMessage());
        rsp.setErrorMessage(msg);

        LOG.error(msg, e);

        return rsp;
    }

    @ExceptionHandler({Throwable.class})
    public BaseRsp cartExceptionHandler(Throwable e) {
        BaseRsp rsp = new BaseRsp();
        rsp.setSuccess(false);
        rsp.setErrorCode(INTERNAL_ERROR);
        String msg = errorMsgWithTrackingId(e.getMessage());
        rsp.setErrorMessage(msg);

        LOG.error(msg, e);

        return rsp;
    }

    private String errorMsgWithTrackingId(String msg) {
        return String.format(ERROR_MSG_TMPL, UUID.randomUUID(), msg);
    }
}

