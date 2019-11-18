package com.active.services.cart.model.v1;

import com.active.services.cart.model.Range;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CartItemDto extends BaseDto {

    private Long productId;

    private String productName;

    private String productDescription;

    private Range<Instant> bookingRange;

    private Range<Instant> trimmedBookingRange;

    private int quantity;

    private BigDecimal unitPrice;

    private String groupingIdentifier;
}
