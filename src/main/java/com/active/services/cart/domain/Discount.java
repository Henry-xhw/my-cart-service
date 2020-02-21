package com.active.services.cart.domain;

import com.active.services.product.AmountType;
import com.active.services.product.DiscountType;

import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
public class Discount {
    private String name;
    private String description;
    private BigDecimal amount;
    private AmountType amountType;
    private Long discountId;
    private DiscountType discountType;
    private String couponCode;

    public Discount(String name, String description, @NonNull BigDecimal amount, @NonNull AmountType amountType) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
    }

    public Discount(String name, String description, @NonNull BigDecimal amount, @NonNull AmountType amountType,
                    Long discountId, DiscountType discountType, String couponCode) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
        this.discountId = discountId;
        this.discountType = discountType;
        this.couponCode = couponCode;
    }
}
