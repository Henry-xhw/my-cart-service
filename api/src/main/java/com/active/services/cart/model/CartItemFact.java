package com.active.services.cart.model;

import lombok.Data;

import java.util.List;

@Data
public class CartItemFact {
    private List<KVFactPair> kvFactPairs;
}
