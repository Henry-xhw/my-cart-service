package com.active.services.cart.domain.rule;

import com.active.services.domain.DateTime;
import com.active.services.product.Fee;
import com.active.services.product.FeeType;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class ProductFact {
    //product fact
    private FeeType feeType;
    private List<Fee> fees;

    //product pricing input
    private DateTime pricingDt;
    private int volume;

    //product pricing result
    private Fee result;

    @Builder
    public ProductFact(FeeType feeType, List<Fee> fees, DateTime pricingDt, int volume) {
        this.feeType = feeType;
        this.fees = fees;
        this.pricingDt = pricingDt;
        this.volume = volume;
    }
}
