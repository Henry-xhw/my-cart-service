package com.active.services.cart.service.checkout;

import com.google.common.eventbus.EventBus;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@RequiredArgsConstructor
public abstract class CheckoutBaseProcessor  {
    protected final CheckoutContext checkoutContext;

    private final CheckoutEvent.CheckoutPhase checkoutPhase;

    @Autowired
    private EventBus eventBus;

    public void process() {
        try {
            doProcess();
        } catch (Exception e) {
            publishFailedEvent(e);
            throw new RuntimeException(e);
        }
    }

    protected abstract void doProcess();

    protected void publishFailedEvent(Exception e) {
        CheckoutEvent event = new CheckoutEvent();
        event.setIdentifier(checkoutContext.getCart().getIdentifier().toString());
        event.setType(checkoutPhase + "_FAILED");
        event.setPayload(e == null ? null: e.getMessage());
        eventBus.post(event);
    }

    protected void publishFailedEvent() {
        this.publishFailedEvent(null);
    }
}
