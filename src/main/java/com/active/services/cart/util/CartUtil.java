package com.active.services.cart.util;

import com.active.services.Context;
import com.active.services.ContextWrapper;
import com.active.services.cart.domain.BaseDomainObject;

import java.time.Instant;

public class CartUtil {

    private CartUtil() {
    }

    public static <T extends BaseDomainObject> T addAuditableAttributes(T target) {
        Context context = ContextWrapper.getValidContext();
        addAuditableAttributes(context, target);

        return target;
    }

    public static <T extends BaseDomainObject> T addAuditableAttributes(Context context, T target) {
        target.setCreatedBy(context.getActorId());
        target.setModifiedBy(context.getActorId());
        Instant curDate = Instant.now();
        target.setCreatedDt(curDate);
        target.setModifiedDt(curDate);

        return target;
    }
}
