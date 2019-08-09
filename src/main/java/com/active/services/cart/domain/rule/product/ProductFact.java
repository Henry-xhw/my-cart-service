package com.active.services.cart.domain.rule.product;

import java.util.List;

import com.active.services.cart.domain.rule.Fact;
import com.active.services.cart.domain.rule.condition.KVFactPairs;
import com.active.services.cart.model.KVFactPair;
import com.active.services.product.Fee;

import lombok.Data;

@Data
public class ProductFact implements Fact {
    private final KVFactPairs factPairs;

    private Fee result;

    public ProductFact(List<KVFactPair> kvFactPairs) {
        factPairs = new KVFactPairs(kvFactPairs);
    }

    @Override
    public <T> T getFact(String key) {
        return (T) factPairs.value(key);
    }
}
