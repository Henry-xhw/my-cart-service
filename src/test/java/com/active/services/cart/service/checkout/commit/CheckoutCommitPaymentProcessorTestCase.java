package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.common.Event;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.mock.MockCart;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.CheckoutBaseProcessor;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

public class CheckoutCommitPaymentProcessorTestCase extends BaseTestCase {

    private EventBus eventBus = Mockito.mock(EventBus.class);

    private CartRepository cartRepository = Mockito.mock(CartRepository.class);

    @Before
    public void setUp() {
        Event.setObjectMapper(new ObjectMapper());
        super.setUp();
    }

    private CheckoutBaseProcessor buildProcessor(CheckoutContext checkoutContext) {
        CheckoutBaseProcessor checkoutBaseProcessor = new CheckoutCommitPaymentProcessor(checkoutContext);
        ReflectionTestUtils.setField(checkoutBaseProcessor, "eventBus", eventBus);
        ReflectionTestUtils.setField(checkoutBaseProcessor, "cartRepository", cartRepository);
        return checkoutBaseProcessor;
    }

    @Test
    public void processSuccess() {
        Mockito.when(cartRepository.finalizeCart(any(), any())).thenReturn(1);
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(MockCart.mockCartDomain());
        List<CartItemFee> cartItemFees = checkoutContext.getCart().getFlattenCartItems().stream()
                .filter(item -> Objects.nonNull(item.getFees())).map(CartItem::getFlattenCartItemFees)
                .flatMap(List::stream).collect(Collectors.toList());
        checkoutContext.setFlattenCartItemFees(cartItemFees);

        buildProcessor(checkoutContext).process();
        verify(cartRepository).finalizeCart(any(), any());
    }
}
