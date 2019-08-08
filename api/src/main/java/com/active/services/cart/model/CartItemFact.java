package com.active.services.cart.model;

import java.util.List;

import lombok.Data;

@Data
public class CartItemFact {
    private List<KVFactPair> kvFactPairs;
}
