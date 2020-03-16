package com.active.services.cart.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
public class AdHocDiscount extends BaseDomainObject {
    private Long cartItemId;
    private String discountName;
    private UUID discountKeyerId;
    private BigDecimal discountAmount;
    private String discountCouponCode;
    private Long discountGroupId;
}
