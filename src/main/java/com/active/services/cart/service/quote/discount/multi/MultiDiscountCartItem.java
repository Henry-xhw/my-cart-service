package com.active.services.cart.service.quote.discount.multi;

import com.active.services.cart.domain.CartItem;
import com.active.services.product.discount.multi.MultiDiscount;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MultiDiscountCartItem {
    private final MultiDiscount multiDiscount;

    private List<CartItem> cartItems = new ArrayList<>();

    public void addCartItems(List<CartItem> newItems) {
        cartItems.addAll(newItems.stream().filter(newItem -> cartItems.stream().noneMatch(existingItem ->
                existingItem.getIdentifier().equals(newItem.getIdentifier()))).collect(Collectors.toList()));
    }
}
