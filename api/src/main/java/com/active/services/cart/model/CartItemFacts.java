package com.active.services.cart.model;

import lombok.Data;

import java.util.List;

@Data
public class CartItemFacts {
    private List<KVFactPair> kvFactPairs;
}
