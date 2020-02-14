package com.active.services.cart.service.quote.discount;

import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountType;

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
    private Long discountId;
    private DiscountType discountType;
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
        return DiscountAmountCalcUtil.calcFlatAmount(amountToDiscount, amount, amountType, currency);
    }
}
