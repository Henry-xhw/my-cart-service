package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class DiscountApplication extends Discount {

    @Setter
    private DiscountSpecification condition;

    public boolean satisfy() {
        return condition.satisfy();
    }
}