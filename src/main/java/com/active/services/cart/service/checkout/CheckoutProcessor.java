package com.active.services.cart.service.checkout;

import com.active.services.cart.common.CartException;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.commit.CheckoutCommitPhaseProcessor;
import com.active.services.cart.service.checkout.prepare.CheckoutPreparePhaseProcessor;
import com.active.services.cart.util.AuditorAwareUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Order checkout flow handling to simulate two phase commit:
 * 1. Prepare phase: validation and lock resources.
 * 2. Commit phase: commit changes to db/other services.
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class CheckoutProcessor {
    @Autowired
    private CartRepository cartRepository;

    private final CheckoutContext checkoutContext;

    public void process() {
        new CheckoutValidator().validate(checkoutContext);

        acquireLock();
        try {
            getCheckoutPreparePhaseProcessor(checkoutContext).process();
            getCheckoutCommitPhaseProcessor(checkoutContext).process();
        } finally {
            releaseLock();
        }
    }

    @Lookup
    public CheckoutPreparePhaseProcessor getCheckoutPreparePhaseProcessor(CheckoutContext checkoutContext) {
        return null;
    }

    @Lookup
    public CheckoutCommitPhaseProcessor getCheckoutCommitPhaseProcessor(CheckoutContext checkoutContext) {
        return null;
    }

    private void acquireLock() {
        UUID cartId = checkoutContext.getCart().getIdentifier();
        boolean lockAcquired = cartRepository.acquireLock(cartId, AuditorAwareUtil.getAuditor()) == 1;
        if (!lockAcquired) {
            LOG.warn("Cart {} had been locked by other call", cartId);
            throw new CartException(ErrorCode.CART_LOCKED, "Cart: {0} had been locked by other call.", cartId);
        }
    }

    private boolean releaseLock() {
        return cartRepository.releaseLock(checkoutContext.getCart().getIdentifier(), AuditorAwareUtil.getAuditor()) == 1;
    }

}
