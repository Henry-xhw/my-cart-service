package com.active.services.cart.service.checkout.commit;

import com.active.services.ContextWrapper;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.CheckoutBaseProcessor;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.cart.service.checkout.CheckoutEvent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class CheckoutCommitPaymentProcessor extends CheckoutBaseProcessor {
    @Autowired
    private CartRepository cartRepository;

    public CheckoutCommitPaymentProcessor(CheckoutContext checkoutContext) {
        super(checkoutContext, CheckoutEvent.CheckoutPhase.COMMIT_PAYMENT);
    }

    @Override
    protected void doProcess() {
        // Should place order to be finalized if payment passed.
        cartRepository.finalizeCart(getCheckoutContext().getCart().getIdentifier(), ContextWrapper.get().getActorId());
    }
}
