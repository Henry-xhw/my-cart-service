package com.active.services.cart.domain.rule.fee;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KVFactPair {
    private String key;
    private Object value;
}
