package com.active.services.cart.service.checkout.commit;

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
@Slf4j
@RequiredArgsConstructor
public class CheckoutCommitPhaseProcessor {

    private final CheckoutContext checkoutContext;

    public void process() {
        getCheckoutPaymentProcessor(checkoutContext).process();
        getCheckoutInventoryProcessor(checkoutContext).process();
        getCheckoutOrderProcessor(checkoutContext).process();
    }

    @Lookup
    public CheckoutCommitPaymentProcessor getCheckoutPaymentProcessor(CheckoutContext checkoutContext) {
        return null;
    }

    @Lookup
    public CheckoutCommitInventoryProcessor getCheckoutInventoryProcessor(CheckoutContext checkoutContext) {
        return null;
    }

    @Lookup
    public CheckoutCommitOrderProcessor getCheckoutOrderProcessor(CheckoutContext checkoutContext) {
        return null;
    }

}
