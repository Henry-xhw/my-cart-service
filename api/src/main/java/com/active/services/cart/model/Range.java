package com.active.services.cart.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Range<T extends Comparable<T>> {
    private T lower;

    private T upper;

    public boolean valid() {
        if (Objects.nonNull(lower) && Objects.nonNull(upper) && lower.compareTo(upper) > 0) {
            return false;
        }
        return true;
    }
}
