package com.active.services.cart.service.checkout;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.Payment;
import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.CartHolder;
import com.active.services.cart.model.CartItemFeeAllocation;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.cart.model.Range;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.domain.Address;
import com.active.services.inventory.rest.dto.DateTimeRange;
import com.active.services.inventory.rest.dto.ReservationDTO;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
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

    private List<CartItemFeeAllocation> feeAllocations;

    private List<Payment> payments = new ArrayList<>();

    public List<ReservationDTO> getReservations() {
        return cart.getFlattenCartItems().stream().map(cartItem -> {
            ReservationDTO reservationDTO = new ReservationDTO();

            reservationDTO.setProductId(cartItem.getProductId());
            reservationDTO.setQuantity(cartItem.getQuantity());
            Range<Instant> br = cartItem.getBookingRange();
            if (br != null && (br.getLower() != null || br.getUpper() != null)) {
                DateTimeRange dateTimeRange = new DateTimeRange();
                dateTimeRange.setStartDateTime(br.getLower());
                dateTimeRange.setEndDateTime(br.getUpper());
                List<DateTimeRange> dateTimeRanges = Collections.singletonList(dateTimeRange);
                reservationDTO.setDateTimeRanges(dateTimeRanges);
            }

            return reservationDTO;
        }).collect(Collectors.toList());
    }
}
