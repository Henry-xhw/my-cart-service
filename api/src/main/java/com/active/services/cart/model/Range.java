package com.active.services.cart.model;

import com.active.services.cart.model.validation.ValidRange;

import lombok.Data;

@Data
@ValidRange
public class Range<T extends Comparable<T>> {
    private T lower;

    private T upper;
}
