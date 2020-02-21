package com.active.services.cart.model.v1;

import com.active.services.cart.model.AmountType;
import com.active.services.cart.model.DiscountOrigin;
import com.active.services.cart.model.DiscountType;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;

public class DiscountDto extends BaseDto {
    private Long cartId;
    private String name;
    private String description;
    @Min(value = 0)
    @Digits(integer = 17, fraction = 2)
    private BigDecimal amount;
    private AmountType amountType;
    private Long discountId;
    private DiscountType discountType;
    private String couponCode;
    private DiscountOrigin origin;
}
