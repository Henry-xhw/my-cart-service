package com.active.services.cart.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KVFactPair {
    private String key;
    private Object value;
}
