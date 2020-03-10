package com.active.services.cart.model;

import com.active.services.cart.model.v1.BaseDto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AdHocDiscountDto extends BaseDto {
    private String adHocDiscountName;
    private UUID adHocDiscountKeyerId;
    @NotNull
    private BigDecimal adHocDiscountAmount;
    private String adHocDiscountCouponCode;
    private Long adHocDiscountGroupId;
}
