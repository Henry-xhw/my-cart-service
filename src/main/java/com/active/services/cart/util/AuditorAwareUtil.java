package com.active.services.cart.util;

import java.util.Optional;

public class AuditorAwareUtil {

    private static final String ACTOR_ID = "Actor-Id";

    public static Optional<String> getAuditor() {
        return Optional.ofNullable(RequestContextUtil.getRequest().getHeader(ACTOR_ID));
    }
}
