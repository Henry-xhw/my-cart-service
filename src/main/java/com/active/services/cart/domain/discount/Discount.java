package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.discount.condition.DiscountSpecification;
import com.active.services.product.AmountType;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;

@Getter
public class Discount {
    private String name;
    private String description;
    private BigDecimal amount;
    private AmountType amountType;
    @Setter private DiscountSpecification condition;

    public Discount(String name, String description, @NonNull BigDecimal amount, @NonNull AmountType amountType) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
    }

    public boolean satisfy() {
        return condition.satisfy();
    }

    public BigDecimal apply(BigDecimal amountToDiscount, Currency currency) {
        return DiscountUtil.getFlatAmount(amountToDiscount, amount, amountType, currency);
    }
}
