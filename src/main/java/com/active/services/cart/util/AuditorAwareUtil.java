package com.active.services.cart.util;

import com.active.services.Context;
import com.active.services.ContextDefaultImpl;

import java.util.Optional;

public class AuditorAwareUtil {

    private static final String ACTOR_ID = "Actor-Id";
    private static final String SYSTEM = "system";

    private AuditorAwareUtil() {

    }

    public static String getAuditor() {
        return Optional.ofNullable(RequestContextUtil.getRequest())
                .map(req -> Optional.ofNullable(req.getHeader(ACTOR_ID)).orElse(SYSTEM))
                .orElse(SYSTEM);
    }

    public static Context getContext() {
        return new ContextDefaultImpl(null, null, null, getAuditor());
    }
}
