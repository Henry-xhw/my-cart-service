package com.active.services.cart.domain;

import com.active.services.cart.model.Range;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CartItem extends BaseDomainObject {

    private Long productId;

    private String productName;

    private String productDescription;

    private Range<Instant> bookingRange;

    private Range<Instant> timmedBookingRange;

    private int quantity;

    private BigDecimal unitPrice;

    private String groupingIdentifier;
}
