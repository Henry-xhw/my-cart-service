package com.active.services.cart.model.v1;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.AdHocDiscountDto;
import com.active.services.cart.model.CouponMode;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@EqualsAndHashCode(callSuper = true)
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
    private BigDecimal overridePrice;

    @Size(max = 255)
    private String groupingIdentifier;

    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal grossPrice;

    /**
     * Net price equals the total of price subtract total price discount,
     * net price is calculated by method getNetPrice. No need for field netPrice
     * OMS-10776
     */

    private Integer feeVolumeIndex;

    @Valid
    private List<CartItemDto> subItems = new ArrayList<>();

    private boolean oversold;

    private Set<String> couponCodes;

    @Size(max = 50)
    private String personIdentifier;

    private boolean ignoreMultiDiscounts;

    private CouponMode couponMode;

    private UUID reservationId;

    private List<AdHocDiscountDto> adHocDiscounts;
}