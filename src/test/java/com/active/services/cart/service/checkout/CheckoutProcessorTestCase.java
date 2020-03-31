package com.active.services.cart.service.checkout;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.commit.CheckoutCommitPhaseProcessor;
import com.active.services.cart.service.checkout.prepare.CheckoutPreparePhaseProcessor;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CheckoutProcessorTestCase extends BaseTestCase {

    private CartRepository cartRepository = mock(CartRepository.class);

    private CheckoutProcessor checkoutProcessor;

    @Before
    public void setUp() {
        super.setUp();
        UUID cartId = UUID.randomUUID();
        Cart cart = getQualifiedCart(cartId);
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);
        checkoutProcessor = spy(new CheckoutProcessor(checkoutContext));
        ReflectionTestUtils.setField(checkoutProcessor, "cartRepository", cartRepository);
    }

    @Test
    public void process() {
        CheckoutPreparePhaseProcessor checkoutPreparePhaseProcessor = mock(CheckoutPreparePhaseProcessor.class);
        CheckoutCommitPhaseProcessor checkoutCommitPhaseProcessor = mock(CheckoutCommitPhaseProcessor.class);
        when(cartRepository.releaseLock(any(), any())).thenReturn(1);
        when(cartRepository.acquireLock(any(), any())).thenReturn(1);
        when(checkoutProcessor.getCheckoutPreparePhaseProcessor(any())).thenReturn(checkoutPreparePhaseProcessor);
        when(checkoutProcessor.getCheckoutCommitPhaseProcessor(any())).thenReturn(checkoutCommitPhaseProcessor);
        checkoutProcessor.process();
    }

    @Test(expected = CartException.class)
    public void processFail() {
        CheckoutPreparePhaseProcessor checkoutPreparePhaseProcessor = mock(CheckoutPreparePhaseProcessor.class);
        CheckoutCommitPhaseProcessor checkoutCommitPhaseProcessor = mock(CheckoutCommitPhaseProcessor.class);
        when(cartRepository.releaseLock(any(), any())).thenReturn(1);
        when(cartRepository.acquireLock(any(), any())).thenReturn(0);
        when(checkoutProcessor.getCheckoutPreparePhaseProcessor(any())).thenReturn(checkoutPreparePhaseProcessor);
        when(checkoutProcessor.getCheckoutCommitPhaseProcessor(any())).thenReturn(checkoutCommitPhaseProcessor);
        checkoutProcessor.process();
    }

    private Cart getQualifiedCart(UUID cartId) {
        Cart cart = CartDataFactory.cart();
        cart.setIdentifier(cartId);
        List<CartItem> items = new ArrayList<>();
        items.add(CartDataFactory.cartItem());
        cart.setItems(items);
        cart.setVersion(1);
        cart.setPriceVersion(1);
        return cart;
    }
}
