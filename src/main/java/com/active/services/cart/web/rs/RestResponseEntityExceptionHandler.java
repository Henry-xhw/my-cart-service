package com.active.services.cart.web.rs;

import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * REST exception handler for ResponseEntity. <br>
 * This handler can handle any type of Exception.
 * If catching {@link Exception}, the 500-INTERNAL_SERVER_ERROR will be returned, and the error will be logged.
 *
 * @author sharryshuai
 */
@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle the specified exception {@link MethodArgumentNotValidException}
     * @param exception {@link MethodArgumentNotValidException}
     * @param headers headers for the response
     * @param status response status
     * @param request current request
     * @return a {@link ResponseEntity} with error messages
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errorMap = exception.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (first, second) -> first));
        return handleExceptionInternal(exception, JSONObject.toJSONString(errorMap), headers, status, request);
    }

    /**
     * Handle common exception {@link Exception}
     * @param exception {@link Exception}
     * @param request {@link WebRequest}
     * @return a {@link ResponseEntity} with error messages
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleBadRequest(final Exception exception, final WebRequest request) {
        return handleExceptionInternal(exception, null, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    /**
     * Customize the response body of any exception types.<br>
     * Creates a {@link ResponseEntity} from the given expcetion message, body, headers, and status.
     * @param exception the exception
     * @param body body for the response
     * @param headers headers for the response
     * @param status response status
     * @param request current request
     * @return a {@link ResponseEntity} with error messages
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
        Exception exception, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            LOG.error("Catched internal error", exception);
        } else {
            LOG.warn("Catched suspicious action", exception);
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(body, headers, status);
    }
}
