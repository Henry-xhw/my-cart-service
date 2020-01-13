package com.active.services.cart.service.checkout.prepare;

import com.active.services.cart.client.rest.ReservationService;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.model.Range;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.inventory.rest.dto.DateTimeRange;
import com.active.services.inventory.rest.dto.ReservationDTO;
import com.active.services.inventory.rest.dto.ReservationResultDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Prepare phase to lock resource for commit.
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class CheckoutInventoryPrepareProcessor {
    private final CheckoutContext checkoutContext;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private CartRepository cartRepository;

    public void process() {
        Cart cart = checkoutContext.getCart();
        UUID reservationId = cart.getReservationId();
        if (reservationId == null) {
            reserveInventory();
        } else {
            reservationService.touchReserve(reservationId);
        }
    }

    private void reserveInventory() {
        Cart cart = checkoutContext.getCart();
        List<ReservationDTO> reservations = cart.getFlattenCartItems().stream().map(cartItem -> {
            ReservationDTO reservationDTO = new ReservationDTO();

            reservationDTO.setProductId(cartItem.getProductId());
            reservationDTO.setQuantity(cartItem.getQuantity());
            Range<Instant> br = cartItem.getBookingRange();
            if (br != null && (br.getLower() != null || br.getUpper() != null)) {
                DateTimeRange dateTimeRange = new DateTimeRange();
                dateTimeRange.setStartDateTime(br.getLower());
                dateTimeRange.setEndDateTime(br.getUpper());
                List<DateTimeRange> dateTimeRanges = Arrays.asList(dateTimeRange);
                reservationDTO.setDateTimeRanges(dateTimeRanges);
            }

            return reservationDTO;
        }).collect(Collectors.toList());

        ReservationResultDTO reservationResult = reservationService.reserve(reservations);
        cart.setReservationId(reservationResult.getReservationId());
        // TODO: handle error case.
        cartRepository.updateCartReservationId(cart);
    }
}
