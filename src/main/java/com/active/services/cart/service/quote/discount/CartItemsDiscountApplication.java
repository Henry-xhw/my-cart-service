package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CartItemsDiscountApplication {
    @NonNull private final List<CartItem> items;
    @NonNull private final List<Discount> discounts;
    @NonNull private final DiscountAlgorithm algorithm;
    @NonNull private final String currency;
}
