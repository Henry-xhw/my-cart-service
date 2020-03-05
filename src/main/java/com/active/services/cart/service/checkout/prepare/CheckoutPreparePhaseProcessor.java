package com.active.services.cart.service.checkout.prepare;

import com.active.services.cart.client.rest.ReservationService;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.inventory.rest.dto.ReservationResultDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Prepare phase to lock resource for commit.
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class CheckoutPreparePhaseProcessor {

    private final CheckoutContext checkoutContext;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ReservationService reservationService;

    public void process() {
        Cart cart = checkoutContext.getCart();
        UUID reservationGroupId = cart.getReservationGroupId();
        if (reservationGroupId == null) {
            reserveInventory();
        } else {
            reservationService.edit(reservationGroupId, checkoutContext.getReservations());
        }
    }

    private void reserveInventory() {
        Cart cart = checkoutContext.getCart();
        ReservationResultDTO reservationResult = reservationService.reserve(checkoutContext.getReservations());
        cart.setReservationGroupId(reservationResult.getReservationGroupId());
        cartRepository.updateCartReservationId(cart.getIdentifier(), cart.getReservationGroupId());
    }
}
