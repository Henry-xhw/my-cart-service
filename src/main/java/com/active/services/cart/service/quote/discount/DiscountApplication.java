package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
public class DiscountApplication extends Discount {

    private DiscountSpecification condition;

    @Builder
    public DiscountApplication(String name, String description, @NonNull BigDecimal amount, @NonNull AmountType amountType,
                    Long discountId, DiscountType discountType, String couponCode, DiscountAlgorithm algorithm,
                    DiscountSpecification condition) {
        super(name, description, amount, amountType, discountId, discountType, couponCode, algorithm);
        this.condition = condition;
    }

    public boolean satisfy() {
        return condition.satisfy();
    }
}