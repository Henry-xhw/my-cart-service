package com.active.services.cart.service.checkout;

import com.active.services.cart.repository.CartRepository;
import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class CheckoutEventSubscriber {

    @Autowired
    private EventBus eventBus;

    @Autowired
    private CartRepository cartRepository;

    @Subscribe
    @AllowConcurrentEvents
    public void handleCheckoutEvent(CheckoutEvent checkoutEvent) {
        cartRepository.createEvents(Arrays.asList(checkoutEvent));
    }

    @PostConstruct
    public void postConstruct() {
        eventBus.register(this);
    }
}
