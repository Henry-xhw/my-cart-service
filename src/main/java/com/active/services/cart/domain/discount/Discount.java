package com.active.services.cart.domain.discount;

import com.active.services.product.AmountType;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Discount {
    private String name;
    private String description;
    private BigDecimal amount;
    private AmountType amountType;
    private DiscountCondition condition;
}
