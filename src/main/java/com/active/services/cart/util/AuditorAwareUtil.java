package com.active.services.cart.util;

import java.util.Optional;

public class AuditorAwareUtil {

    private static final String ACTOR_ID = "Actor-Id";
    private static final String SYSTEM = "system";

    public static String getAuditor() {
        return Optional.ofNullable(RequestContextUtil.getRequest().getHeader(ACTOR_ID)).orElse(SYSTEM);
    }
}
