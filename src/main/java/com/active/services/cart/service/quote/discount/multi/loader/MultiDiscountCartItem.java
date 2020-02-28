package com.active.services.cart.service.quote.discount.multi.loader;

import com.active.services.cart.domain.CartItem;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountComparator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class MultiDiscountCartItem implements Comparable<MultiDiscountCartItem> {
    private final MultiDiscount multiDiscount;

    private List<CartItem> cartItems = new ArrayList<>();

    public void addCartItems(List<CartItem> newItems) {
        cartItems.addAll(newItems.stream().filter(newItem -> cartItems.stream().noneMatch(existingItem ->
            existingItem.getIdentifier().equals(newItem.getIdentifier()))).collect(Collectors.toList()));
    }

    @Override
    public int compareTo(MultiDiscountCartItem other) {
        return new MultiDiscountComparator().compare(getMultiDiscount(), other.getMultiDiscount());
    }
}
