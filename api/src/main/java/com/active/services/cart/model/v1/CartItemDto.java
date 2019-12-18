package com.active.services.cart.model.v1;

import java.math.BigDecimal;
import java.time.Instant;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.active.services.cart.model.Range;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class CartItemDto extends BaseDto {

    @NotNull
    private Long productId;

    @NotBlank
    @Size(max = 255)
    private String productName;

    private String productDescription;

    @Valid
    private Range<Instant> bookingRange;

    @Valid
    private Range<Instant> trimmedBookingRange;

    @NotNull
    @Min(value = 1)
    private Integer quantity;

    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal unitPrice;

    @Size(max = 255)
    private String groupingIdentifier;

    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal grossPrice;

    /**
     * Net Price = Gross Price - Price Hikes Amount - Discounts Amount
     */
    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal netPrice;

    private Integer feeVolumeIndex;

    private Long agencyId;

    @Valid
    private List<CartItemFeeDto> fees = new ArrayList<>();
}