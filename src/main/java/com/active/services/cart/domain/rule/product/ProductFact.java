package com.active.services.cart.domain.rule.product;

import java.util.List;

import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.fee.KVFactPairs;
import com.active.services.domain.DateTime;
import com.active.services.product.Fee;
import com.active.services.product.FeeType;

import lombok.Builder;
import lombok.Data;

@Data
public class ProductFact implements Fact {
    //product fact
    private List<Fee> fees;

    private KVFactPairs factPairs;

    //product pricing result
    private Fee result;

    @Builder
    public ProductFact(FeeType feeType, List<Fee> fees, DateTime pricingDt, int volume) {
        this.fees = fees;
        if (factPairs == null) {
            factPairs = new KVFactPairs();
        }
        factPairs.add("feeType", feeType)
                .add("pricingDt", pricingDt)
                .add("volume", volume);
    }

    @Override
    public <T> T getFact(String key) {
        return (T) factPairs.value(key);
    }
}
