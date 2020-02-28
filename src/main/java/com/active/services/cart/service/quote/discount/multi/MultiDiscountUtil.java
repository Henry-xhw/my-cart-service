package com.active.services.cart.service.quote.discount.multi;

import com.active.services.cart.domain.CartItem;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

public final class MultiDiscountUtil {
    private MultiDiscountUtil() {

    }

    public static int uniquePerson(List<CartItem> cartItems) {
        return Long.valueOf(cartItems.stream().map(CartItem::getPersonIdentifier).distinct().count()).intValue();
    }

    public static int quantityCounts(List<CartItem> cartItems) {
        return cartItems.stream().map(CartItem::getQuantity).reduce(Integer::sum).orElse(0);
    }

    public static Map<String, List<CartItem>> itemsByPersonIdentifier(List<CartItem> cartItems) {
        return cartItems.stream().collect(groupingBy(CartItem::getPersonIdentifier));
    }
}
