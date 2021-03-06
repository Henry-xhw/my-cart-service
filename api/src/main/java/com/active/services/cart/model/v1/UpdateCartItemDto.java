package com.active.services.cart.model.v1;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.AdHocDiscountDto;
import com.active.services.cart.model.CouponMode;

import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
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
public class UpdateCartItemDto {

    @NotNull(message = "cartItem's identifier can not be null")
    private UUID identifier;

    private Long parentId;

    private Long productId;

    @NotBlank(message = "cartItem's product name can not be blank")
    @Size(max = 255)
    private String productName;

    private String productDescription;

    @Valid
    private Range<Instant> bookingRange;

    @Valid
    private Range<Instant> trimmedBookingRange;

    @NotNull(message = "cartItem's quantity can not be null")
    @Min(value = 1, message = "cartItem's quantity can not less than 1")
    private Integer quantity;

    @Min(value = 0, message = "cartItem's override price can not less than 0")
    @Digits(integer = 17, fraction = 2)
    private BigDecimal overridePrice;

    @Size(max = 255)
    private String groupingIdentifier;

    private Integer feeVolumeIndex;

    private boolean oversold;

    private Set<String> couponCodes;

    @Size(max = 50)
    private String personIdentifier;

    private boolean ignoreMultiDiscounts;

    private CouponMode couponMode;

    private UUID reservationId;

    @Valid
    private List<AdHocDiscountDto> adHocDiscounts;
}
