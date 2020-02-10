package com.active.services.cart.service.checkout;

import com.active.platform.types.range.Range;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.CartHolder;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.domain.Address;
import com.active.services.inventory.rest.dto.DateTimeRange;
import com.active.services.inventory.rest.dto.ReservationDTO;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class CheckoutContext {
    private PaymentAccount paymentAccount;

    private boolean sendReceipt = true;

    private String orderUrl;

    private Address billingAddress;

    private BillingContact billingContact;

    private CartHolder cartHolder;

    private Cart cart;

    private List<CheckoutResult> checkoutResults = new ArrayList<>();

    public List<ReservationDTO> getReservations() {
        List<ReservationDTO> reservations = cart.getFlattenCartItems().stream().map(cartItem -> {
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setAllowOversold(cartItem.isOversold());
            reservationDTO.setProductId(cartItem.getProductId());
            reservationDTO.setQuantity(cartItem.getQuantity());

            Range<Instant> br = cartItem.getBookingRange();
            if (br != null && (br.getLowerInclusive() != null || br.getUpperExclusive() != null)) {
                DateTimeRange dateTimeRange = new DateTimeRange();
                dateTimeRange.setStartDateTime(br.getLowerInclusive());
                dateTimeRange.setEndDateTime(br.getUpperExclusive());
                List<DateTimeRange> dateTimeRanges = Arrays.asList(dateTimeRange);
                reservationDTO.setDateTimeRanges(dateTimeRanges);
            }

            return reservationDTO;
        }).collect(Collectors.toList());

        return reservations;
    }
}
