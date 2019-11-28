package com.active.services.cart.common.exception;

import com.active.services.cart.model.v1.ErrorBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedRuntimeException;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({CartException.class})
    public ErrorBody CartExceptionHandler(HttpServletRequest req, CartException e) {
        LOG.error("The error message is {}", e.getMessage());
        return handleErrorInfo(e.getErrorCode(), req.getRequestURL().toString(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ErrorBody ExceptionHandler(HttpServletRequest req, Exception e) {
        LOG.error("The error message is {}", e.getMessage());
        if (e instanceof NestedRuntimeException || e instanceof MethodArgumentNotValidException
                || e instanceof ConstraintViolationException) {
            return handleErrorInfo(HttpStatus.BAD_REQUEST.value(), req.getRequestURL().toString(), e.getMessage());
        } else if (e instanceof HttpMediaTypeException) {
            return handleErrorInfo(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(), req.getRequestURL().toString(), e.getMessage());
        } else if (e instanceof HttpRequestMethodNotSupportedException) {
            return handleErrorInfo(HttpStatus.METHOD_NOT_ALLOWED.value(), req.getRequestURL().toString(), e.getMessage());
        } else {
            return handleErrorInfo(HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    req.getRequestURL().toString(), e.getMessage());
        }
    }

    private ErrorBody handleErrorInfo(int errorCode, String path, String errorMsg) {
        return ErrorBody.builder()
                .errorCode(errorCode)
                .path(path)
                .errorMsg(errorMsg)
                .build();
    }
}

