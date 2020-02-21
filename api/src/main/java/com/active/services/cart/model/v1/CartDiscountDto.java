package com.active.services.cart.model.v1;

import com.active.services.cart.model.AmountType;
import com.active.services.cart.model.DiscountOrigin;
import com.active.services.cart.model.DiscountType;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

public class CartDiscountDto extends BaseDto {
    private Long cartId;

    private DiscountType discountType;

    private Boolean applyToRecurringBilling = false;

    private Long discountId;

    private UUID keyerUUID;

    private Long discountGroupId;

    private AmountType amountType;

    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal amount;

    private DiscountOrigin origin;
}
