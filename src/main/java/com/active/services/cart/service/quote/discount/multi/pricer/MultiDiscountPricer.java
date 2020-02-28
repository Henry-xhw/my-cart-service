package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public interface MultiDiscountPricer {
    void price();

    void setQuoteContext(CartQuoteContext context);

    /**
     * Sort by total price, then by db id.
     *
     * @param cartItems
     * @return
     */
    default List<CartItem> sortByTotalPrice(List<CartItem> cartItems, boolean asc) {
        Comparator<CartItem> comparator = Comparator.comparing(CartItem::getNetAmount).thenComparing(CartItem::getId);
        if (!asc) {
            comparator = comparator.reversed();
        }
        Collections.sort(cartItems, comparator);

        return cartItems;
    }
}
