package com.active.services.cart.model.v1;

import com.active.services.cart.model.Range;
import lombok.Data;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CartItemDto extends BaseDto {

    private Long productId;

    private String productName;

    private String productDescription;

    @Valid
    private Range<Instant> bookingRange;

    @Valid
    private Range<Instant> trimmedBookingRange;

    private int quantity;

    private BigDecimal unitPrice;

    private String groupingIdentifier;
}
