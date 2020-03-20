package com.active.services.cart.service.checkout;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.google.common.eventbus.EventBus;

import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

public class CheckoutBaseProcessorTestCase {
    @Mock
    private EventBus eventBus;

    private CheckoutBaseProcessor checkoutBaseProcessor;

    @Before
    public void setUp() {
        CheckoutContext checkoutContext = new CheckoutContext();
        Cart cart = CartDataFactory.cart();
        checkoutContext.setCart(cart);
        checkoutBaseProcessor = new CheckoutBaseProcessor(checkoutContext, CheckoutEvent.CheckoutPhase.COMMIT_INVENTORY) {
            @SneakyThrows
            @Override
            protected void doProcess() {
                throw new Exception("exception");
            }
        };
        ReflectionTestUtils.setField(checkoutBaseProcessor, "eventBus", eventBus);
    }

    @Test(expected = RuntimeException.class)
    public void process() {
        checkoutBaseProcessor.process();
    }
}
