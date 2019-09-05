package com.active.services.cart.domain.rule.condition;

import com.active.services.cart.model.FactKVPair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FactKVPairs {
    private Map<String, Object> keyToValue = new HashMap<>();

    public FactKVPairs(List<FactKVPair> pairs) {
        for (FactKVPair kv : pairs) {
            keyToValue.put(kv.getKey(), kv.getValue());
        }
    }

    public FactKVPairs add(String key, Object value) {
        keyToValue.put(key, value);
        return this;
    }

    public Object value(String key) {
        return keyToValue.get(key);
    }
}
