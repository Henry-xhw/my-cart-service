package com.active.services.cart.infrastructure.config;

public class ServiceProxyException extends RuntimeException {
    public ServiceProxyException(Exception e) {
        super(e);
    }
}
