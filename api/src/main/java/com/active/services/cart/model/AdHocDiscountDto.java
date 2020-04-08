package com.active.services.cart.model;

import com.active.services.cart.model.v1.BaseDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AdHocDiscountDto extends BaseDto {
    private String discountName;
    private UUID discountKeyerId;
    @NotNull
    @Min(value = 0)
    private BigDecimal discountAmount;
    private String discountCouponCode;
    private Long discountGroupId;
}
