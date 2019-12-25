package com.active.services.cart.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

public class RequestContextUtil {

    private RequestContextUtil() {
    }

    public static HttpServletRequest getRequest() {
        return Optional.ofNullable((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .map(atr -> atr.getRequest()).orElse(null);
    }
}
