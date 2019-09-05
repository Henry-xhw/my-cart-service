package com.active.services.cart.domain.rule.product;

import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.condition.FactKVPairs;
import com.active.services.cart.model.FactKVPair;
import com.active.services.product.Fee;

import lombok.Data;

import java.util.List;

@Data
public class ProductFact implements Fact {
    private final FactKVPairs factPairs;

    private Fee result;

    public ProductFact(List<FactKVPair> factKVPairs) {
        factPairs = new FactKVPairs(factKVPairs);
    }

    @Override
    public <T> T getFact(String key) {
        return (T) factPairs.value(key);
    }
}
