package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.rest.ReservationService;
import com.active.services.cart.common.Event;
import com.active.services.cart.mock.MockCart;
import com.active.services.cart.service.checkout.CheckoutBaseProcessor;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class CheckoutCommitInventoryProcessorTestCase extends BaseTestCase {

    private EventBus eventBus = Mockito.mock(EventBus.class);

    private ReservationService reservationService = Mockito.mock(ReservationService.class);

    @Before
    public void setUp() {
        Event.setObjectMapper(new ObjectMapper());
        super.setUp();
    }

    private CheckoutBaseProcessor buildProcessor(CheckoutContext checkoutContext) {
        CheckoutBaseProcessor checkoutBaseProcessor = new CheckoutCommitInventoryProcessor(checkoutContext);
        ReflectionTestUtils.setField(checkoutBaseProcessor, "eventBus", eventBus);
        ReflectionTestUtils.setField(checkoutBaseProcessor, "reservationService", reservationService);
        return checkoutBaseProcessor;
    }

    @Test
    public void processSuccess() {
        doNothing().when(reservationService).commit(any());
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(MockCart.mockCartDomain());
        buildProcessor(checkoutContext).process();
        verify(reservationService).commit(any());
    }
}
