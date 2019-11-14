package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.discount.algorithm.DiscountAlgorithm;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Currency;
import java.util.List;

@RequiredArgsConstructor
public class CartItemsDiscountApplication {
    @NonNull private final List<CartItem> items;
    @NonNull private final List<Discount> discounts;
    @NonNull private final DiscountAlgorithm algorithm;
    @NonNull private final Currency currency;
}
