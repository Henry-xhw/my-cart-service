package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.rest.OrderService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.common.Event;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeAllocation;
import com.active.services.cart.service.CartService;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.cart.service.checkout.CheckoutEvent;
import com.active.services.order.management.api.v3.types.OrderResponseDTO;
import com.active.services.order.management.api.v3.types.PlaceOrderReq;
import com.active.services.order.management.api.v3.types.PlaceOrderRsp;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(SpringRunner.class)
public class CheckoutCommitOrderProcessorTestCase extends BaseTestCase {

    @InjectMocks
    private CheckoutCommitOrderProcessor commitOrderProcessor;

    private OrderService orderService = mock(OrderService.class);

    private CheckoutContext checkoutContext = new CheckoutContext();

    private EventBus eventBus = mock(EventBus.class);

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(commitOrderProcessor, "orderService", orderService);
        ReflectionTestUtils.setField(commitOrderProcessor, "checkoutContext", checkoutContext);
        ReflectionTestUtils.setField(commitOrderProcessor, "checkoutPhase", CheckoutEvent.CheckoutPhase.COMMIT_ORDER);
        ReflectionTestUtils.setField(commitOrderProcessor, "eventBus", eventBus);

        ObjectMapper objectMapper = mock(ObjectMapper.class);
        Event.setObjectMapper(objectMapper);
        super.setUp();
    }

    @Test(expected = CartException.class)
    public void testDoProcessFailed() {
        Cart cart = CartDataFactory.cart();
        checkoutContext.setCart(cart);
        checkoutContext.setOrderUrl("order url");
        checkoutContext.setSendReceipt(true);
        PlaceOrderRsp mockOrderRsp = mock(PlaceOrderRsp.class);
        when(orderService.placeOrder(any(PlaceOrderReq.class))).thenReturn(mockOrderRsp);
        commitOrderProcessor.doProcess();
    }

    @Test
    public void testDoProcessSuccess() {
        Cart cart = CartDataFactory.cart();
        checkoutContext.setCart(cart);
        checkoutContext.setOrderUrl("order url");
        checkoutContext.setSendReceipt(true);

        List<CartItemFee> cartItemFees = cart.getFlattenCartItems().stream()
                .filter(item -> Objects.nonNull(item.getFees())).map(CartItem::getFlattenCartItemFees)
                .flatMap(List::stream).collect(Collectors.toList());
        List<CartItemFeeAllocation> feeAllocations = new ArrayList<>();
        cartItemFees.stream().forEach(fee -> {
            CartItemFeeAllocation feeAllocation = new CartItemFeeAllocation();
            feeAllocation.setCartItemFeeIdentifier(fee.getIdentifier());
            feeAllocation.setAmount(fee.getDueAmount());
            feeAllocations.add(feeAllocation);
        });

        PlaceOrderRsp mockOrderRsp = mock(PlaceOrderRsp.class);
        OrderResponseDTO orderResponseDTO =
                OrderResponseDTO.builder().orderId(1L).orderNumber("C-2020021518001").agencyId(1L).build();
        List<OrderResponseDTO> orderResponses = Arrays.asList(orderResponseDTO);
        when(mockOrderRsp.getOrderResponses()).thenReturn(orderResponses);
        when(mockOrderRsp.getSuccess()).thenReturn(true);
        when(orderService.placeOrder(any(PlaceOrderReq.class))).thenReturn(mockOrderRsp);
        commitOrderProcessor.doProcess();
        assertThat(checkoutContext.getCheckoutResults().size()).isEqualTo(1);
    }
}
