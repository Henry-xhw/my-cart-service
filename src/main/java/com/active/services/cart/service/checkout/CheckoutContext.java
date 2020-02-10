package com.active.services.cart.service.checkout;

import com.active.platform.types.range.Range;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.Payment;
import com.active.services.cart.model.BillingContact;
import com.active.services.cart.model.CartHolder;
import com.active.services.cart.model.CartItemFeeAllocation;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.domain.Address;
import com.active.services.inventory.rest.dto.DateTimeRange;
import com.active.services.inventory.rest.dto.ReservationDTO;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

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

    private List<CartItemFee> flattenCartItemFees;

    public List<ReservationDTO> getReservations() {
        return cart.getFlattenCartItems().stream().map(cartItem -> {
            ReservationDTO reservationDTO = new ReservationDTO();
            reservationDTO.setProductId(cartItem.getProductId());
            reservationDTO.setQuantity(cartItem.getQuantity());
            Range<Instant> br = cartItem.getBookingRange();
            if (br != null && (br.getLowerInclusive() != null || br.getUpperExclusive() != null)) {
                DateTimeRange dateTimeRange = new DateTimeRange();
                dateTimeRange.setStartDateTime(br.getLowerInclusive());
                dateTimeRange.setEndDateTime(br.getUpperExclusive());
                List<DateTimeRange> dateTimeRanges = Collections.singletonList(dateTimeRange);
                reservationDTO.setDateTimeRanges(dateTimeRanges);
            }

            return reservationDTO;
        }).collect(Collectors.toList());
    }

    public BigDecimal getTotalDueAmount() {
        return emptyIfNull(flattenCartItemFees).stream()
                .map(fee -> fee.getDueAmount().multiply(BigDecimal.valueOf(fee.getUnits())))
                .reduce((one, two) -> one.add(two)).orElse(BigDecimal.ZERO);
    }

}
