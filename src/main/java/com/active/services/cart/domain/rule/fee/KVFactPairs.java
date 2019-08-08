package com.active.services.cart.domain.rule.fee;

import java.util.HashMap;
import java.util.Map;

public class KVFactPairs {
    private Map<String, Object> keyToValue = new HashMap<>();

    public KVFactPairs(KVFactPair...pairs) {
        for (KVFactPair kv : pairs) {
            keyToValue.put(kv.getKey(), kv.getValue());
        }
    }

    public KVFactPairs add(String key, Object value) {
        keyToValue.put(key, value);
        return this;
    }

    public Object value(String key) {
        return keyToValue.get(key);
    }
}