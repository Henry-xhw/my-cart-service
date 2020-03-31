package com.active.services.cart.service.checkout;

import com.active.services.cart.common.CartException;
import com.active.services.cart.util.CartUtil;
import com.google.common.base.Throwables;
import com.google.common.eventbus.EventBus;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public abstract class CheckoutBaseProcessor  {
    private final CheckoutContext checkoutContext;

    private final CheckoutEvent.CheckoutPhase checkoutPhase;

    @Autowired
    private EventBus eventBus;

    public void process() {
        try {
            doProcess();
        } catch (Exception e) {
            publishFailedEvent(e);
            Throwables.propagateIfPossible(e, CartException.class);
            throw new RuntimeException(e);
        }
    }

    protected CheckoutContext getCheckoutContext() {
        return checkoutContext;
    }

    protected abstract void doProcess();

    protected void publishFailedEvent(Exception e) {
        CheckoutEvent event = new CheckoutEvent();
        event.setBizIdentifier(checkoutContext.getCart().getIdentifier().toString());
        event.setType(checkoutPhase + "_FAILED");
        event.setPayload(e == null ? null : e.getMessage());
        CartUtil.addAuditableAttributes(event);
        eventBus.post(event);
    }

    protected void publishFailedEvent() {
        this.publishFailedEvent(null);
    }
}
