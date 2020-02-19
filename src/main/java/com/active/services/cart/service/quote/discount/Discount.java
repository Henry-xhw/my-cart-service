package com.active.services.cart.service.quote.discount;

import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountAlgorithm;
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
    private String couponCode;
    private DiscountAlgorithm algorithm;
    @Setter private DiscountSpecification condition;

    public Discount(String name, String description, @NonNull BigDecimal amount, @NonNull AmountType amountType) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
    }

    public Discount(String name, String description, @NonNull BigDecimal amount, @NonNull AmountType amountType,
                    Long discountId, DiscountType discountType, String couponCode, DiscountAlgorithm algorithm) {
        this.name = name;
        this.description = description;
        this.amount = amount;
        this.amountType = amountType;
        this.discountId = discountId;
        this.discountType = discountType;
        this.couponCode = couponCode;
        this.algorithm = algorithm;
    }

    public boolean satisfy() {
        return condition.satisfy();
    }

    public BigDecimal apply(BigDecimal amountToDiscount, Currency currency) {
        return DiscountAmountCalcUtil.calcFlatAmount(amountToDiscount, amount, amountType, currency);
    }
}
