package com.active.services.cart.service.checkout.prepare;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.rest.ReservationService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.inventory.rest.dto.ReservationDTO;
import com.active.services.inventory.rest.dto.ReservationResultDTO;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CheckoutPreparePhaseProcessorTestCase  extends BaseTestCase{

    private CartRepository cartRepository = mock(CartRepository.class);

    private ReservationService reservationService = mock(ReservationService.class);

    private CheckoutPreparePhaseProcessor checkoutPreparePhaseProcessor;

    private ReservationResultDTO reservationResultDTO = new ReservationResultDTO();

    private CheckoutContext checkoutContext;

    @Before
    public void setUp() {
        super.setUp();
        UUID cartId = UUID.randomUUID();
        Cart cart = getQualifiedCart(cartId);
        checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);
        checkoutPreparePhaseProcessor = spy(new CheckoutPreparePhaseProcessor(checkoutContext));
        ReflectionTestUtils.setField(checkoutPreparePhaseProcessor, "cartRepository", cartRepository);
        ReflectionTestUtils.setField(checkoutPreparePhaseProcessor, "reservationService", reservationService);
    }

    @Test
    public void process() {
        when(reservationService.edit(any(), any())).thenReturn(reservationResultDTO);
        when(reservationService.reserve(any())).thenReturn(reservationResultDTO);
        checkoutPreparePhaseProcessor.process();
    }

    @Test(expected = CartException.class)
    public void processFail() {
        ReservationDTO reservationDTO = new ReservationDTO();
        List<ReservationDTO> lists = Arrays.asList(reservationDTO);
        reservationResultDTO.setFailedReservations(lists);
        when(reservationService.edit(any(), any())).thenReturn(reservationResultDTO);
        when(reservationService.reserve(any())).thenReturn(reservationResultDTO);
        doNothing().when(cartRepository).updateCartReservationGroupId(any(), any());
        checkoutPreparePhaseProcessor.process();
    }

    @Test
    public void processWithoutReservationGroupId() {
        checkoutContext.getCart().setReservationGroupId(null);
        checkoutPreparePhaseProcessor = spy(new CheckoutPreparePhaseProcessor(checkoutContext));
        ReflectionTestUtils.setField(checkoutPreparePhaseProcessor, "cartRepository", cartRepository);
        ReflectionTestUtils.setField(checkoutPreparePhaseProcessor, "reservationService", reservationService);
        when(reservationService.reserve(any())).thenReturn(new ReservationResultDTO());
        doNothing().when(cartRepository).updateCartReservationGroupId(any(), any());
        checkoutPreparePhaseProcessor.process();
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
