package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.discount.multi.MultiDiscountAlgorithm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static com.active.services.cart.service.quote.discount.multi.MultiDiscountUtil.itemsByPersonIdentifier;
import static java.util.stream.Collectors.toList;

/**
 *
 * Filter and sort cart items by different multi discount algorithm.
 *
 */
@RequiredArgsConstructor
@Slf4j
public class PersonBasedEffectiveMdCartItemBuilder implements Builder<List<CartItem>> {
    private final MultiDiscountCartItem mdCartItem;

    private Comparator<List<CartItem>> allProductsComparator;

    public PersonBasedEffectiveMdCartItemBuilder allProductsComparator(
            Comparator<List<CartItem>> allProductsComparator) {
        this.allProductsComparator = allProductsComparator;

        return this;
    }

    @Override
    public final List<CartItem> build() {
        MultiDiscountAlgorithm algorithm = mdCartItem.getMultiDiscount().getAlgorithm();
        switch (algorithm) {
            case LEAST_EXPENSIVE:
                return buildXXExpensiveItems(true);
            case MOST_EXPENSIVE:
                return buildXXExpensiveItems(false);
            case ALL_PRODUCTS:
                return buildAllProductsEffectiveSortedCartItems();
            default:
                throw new IllegalStateException("Not supported MultiDiscountAlgorithm " + algorithm);
        }
    }

    /**
     * Sort ALL_RPODUCTS algorithm
     *
     * @return
     */
    private List<CartItem> buildAllProductsEffectiveSortedCartItems() {
        Map<String, List<CartItem>> itemsByIdentifier = itemsByPersonIdentifier(mdCartItem.getCartItems());
        List<List<CartItem>> cartItemss = new ArrayList<>(itemsByIdentifier.values());
        Collections.sort(cartItemss, allProductsComparator);

        return cartItemss.stream().flatMap(List::stream).collect(toList());
    }

    /**
     * Build most/least expensive algorithm items.
     *
     * @param asc: price asc flag
     */
    private List<CartItem> buildXXExpensiveItems(boolean asc) {
        List<CartItem> results = new ArrayList<>();
        Map<String, List<CartItem>> itemsByPersonIdentifier = itemsByPersonIdentifier(mdCartItem.getCartItems());

        itemsByPersonIdentifier.forEach((personIdentifier, items) -> {
            CartItem selectedItem = totalPriceThenDbIndex(items, asc).get(0);
            results.add(selectedItem);

            if (items.size() > 1) {
                LOG.debug("Ignore following cart items for person: {}", personIdentifier);
                items.forEach(item -> {
                    if (!item.equals(selectedItem)) {
                        LOG.debug(item.getIdentifier().toString());
                    }
                });
            }
        });
        results.sort(Comparator.comparing(CartItem::getId));
        return results;
    }

    /**
     * Sort by total price, then by db id.
     *
     * @param cartItems
     * @return
     */
    private List<CartItem> totalPriceThenDbIndex(List<CartItem> cartItems, boolean asc) {
        Comparator<CartItem> comparator = Comparator.comparing(CartItem::getNetAmount); // by total price
        if (!asc) {
            comparator = comparator.reversed();
        }
        comparator = comparator.thenComparing(CartItem::getId); // by min db index

        Collections.sort(cartItems, comparator);

        return cartItems;
    }
}
