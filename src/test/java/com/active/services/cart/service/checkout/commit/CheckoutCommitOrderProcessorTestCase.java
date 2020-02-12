package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.rest.OrderService;
import com.active.services.cart.common.Event;
import com.active.services.cart.mock.MockCart;
import com.active.services.cart.service.checkout.CheckoutBaseProcessor;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.order.management.api.v3.types.OrderResponseDTO;
import com.active.services.order.management.api.v3.types.PlaceOrderRsp;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CheckoutCommitOrderProcessorTestCase extends BaseTestCase {

    private EventBus eventBus = Mockito.mock(EventBus.class);

    private OrderService orderService = Mockito.mock(OrderService.class);

    @Before
    public void setUp() {
        Event.setObjectMapper(new ObjectMapper());
        super.setUp();
    }

    private CheckoutBaseProcessor buildProcessor(CheckoutContext checkoutContext) {
        CheckoutBaseProcessor checkoutBaseProcessor = new CheckoutCommitOrderProcessor(checkoutContext);
        ReflectionTestUtils.setField(checkoutBaseProcessor, "eventBus", eventBus);
        ReflectionTestUtils.setField(checkoutBaseProcessor, "orderService", orderService);
        return checkoutBaseProcessor;
    }

    @Test
    public void processSuccess() {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(1L);
        List<OrderResponseDTO> orderResponses = Arrays.asList(orderResponseDTO);
        PlaceOrderRsp placeOrderRsp = new PlaceOrderRsp();
        placeOrderRsp.setOrderResponses(orderResponses);
        Mockito.when(orderService.placeOrder(any())).thenReturn(placeOrderRsp);
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(MockCart.mockCartDomain());
        buildProcessor(checkoutContext).process();
        Assert.assertEquals(orderResponseDTO.getOrderId(), checkoutContext.getCheckoutResults().get(0).getOrderId());
    }

    @Test(expected = RuntimeException.class)
    public void processFailWhenOrderResponsesIsEmpty() {
        Mockito.when(orderService.placeOrder(any())).thenReturn(new PlaceOrderRsp());
        doNothing().when(eventBus).post(any());
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(MockCart.mockCartDomain());
        buildProcessor(checkoutContext).process();
        verify(eventBus, times(2)).post(any());
    }

    @Test(expected = RuntimeException.class)
    public void processFailWhenOrderResponseSuccessIsFalse() {
        OrderResponseDTO orderResponseDTO = new OrderResponseDTO();
        orderResponseDTO.setOrderId(1L);
        List<OrderResponseDTO> orderResponses = Arrays.asList(orderResponseDTO);
        PlaceOrderRsp placeOrderRsp = new PlaceOrderRsp();
        placeOrderRsp.setOrderResponses(orderResponses);
        placeOrderRsp.setSuccess(false);
        Mockito.when(orderService.placeOrder(any())).thenReturn(placeOrderRsp);
        doNothing().when(eventBus).post(any());
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(MockCart.mockCartDomain());
        buildProcessor(checkoutContext).process();
        verify(eventBus, times(2)).post(any());
    }
}
