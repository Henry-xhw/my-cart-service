package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.discount.condition.DiscountSpecification;
import com.active.services.product.AmountType;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Discount {
    private String name;
    private String description;
    private BigDecimal amount;
    private AmountType amountType;
    private DiscountSpecification condition;

    private BigDecimal appliedAmt;

    public Discount(String name, String description, BigDecimal amount, AmountType amountType) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
    }

    public boolean satisfy() {
        return condition.satisfy();
    }
}
