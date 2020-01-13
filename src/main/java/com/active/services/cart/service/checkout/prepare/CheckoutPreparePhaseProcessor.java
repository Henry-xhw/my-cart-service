package com.active.services.cart.service.checkout.prepare;

import com.active.services.cart.service.checkout.CheckoutContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Prepare phase to lock resource for commit.
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class CheckoutPreparePhaseProcessor {
    private final CheckoutContext checkoutContext;

    public void process() {
        getCheckoutInventoryPrepareProcessor(checkoutContext).process();
    }

    @Lookup
    public CheckoutInventoryPrepareProcessor getCheckoutInventoryPrepareProcessor(CheckoutContext checkoutContext) {
        return null;
    }
}
