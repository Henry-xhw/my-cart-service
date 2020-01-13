package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.service.checkout.CheckoutContext;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class CheckoutCommitPaymentProcessor {
    private final CheckoutContext checkoutContext;

    public void process() {

    }
}
