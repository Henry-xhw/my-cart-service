package com.active.services.cart.service.checkout;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class CheckoutEventSubscriber {

    @Autowired
    private EventBus eventBus;

    @Subscribe
    @AllowConcurrentEvents
    public void handleCheckoutEvent(CheckoutEvent checkoutEvent) {
        LOG.info(checkoutEvent.getIdentifier() + checkoutEvent.getType());
    }

    @PostConstruct
    public void postConstruct() {
        eventBus.register(this);
    }
}
