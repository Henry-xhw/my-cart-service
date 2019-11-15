package com.active.services.cart.model;

import lombok.Data;

@Data
public class Range<T> {
    private T lower;

    private T upper;
}
