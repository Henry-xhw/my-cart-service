package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.multi.CartItemMultiDiscountPricer;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.MultiDiscountAlgorithm;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;
import com.active.services.product.discount.multi.SortedTierListBuilder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.active.services.cart.service.quote.discount.multi.MultiDiscountUtil.itemsByPersonIdentifier;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Getter
public abstract class MultiDiscountBasePricer implements MultiDiscountPricer {
    private final MultiDiscountCartItem mdCartItem;

    private final MultiDiscountThresholdSetting effectiveThresholdSetting;

    private CartQuoteContext context;

    @Override
    public final void price() {
        // Step1: build effective cart items by different algorithm.
        List<CartItem> effectiveSortedMdCartItems = buildEffectiveSortedMdCartItems();

        // Step2: apply tiers
        applyTiers(effectiveSortedMdCartItems);
    }

    private void applyTiers(List<CartItem> effectiveSortedMdCartItems) {
        List<DiscountTier> sortedTiers = new SortedTierListBuilder(mdCartItem.getMultiDiscount(), effectiveThresholdSetting,
                countTiers(effectiveSortedMdCartItems)).build();
        Iterator<DiscountTier> sortedTiersIterator = sortedTiers.iterator();

        CartItem prevItem = null;
        DiscountTier currentTier = sortedTiersIterator.next();
        for (CartItem effectiveSortedMdCartItem : effectiveSortedMdCartItems) {
            if (prevItem != null && shouldAdvanceTier(prevItem, effectiveSortedMdCartItem)) {
                currentTier = sortedTiersIterator.next();
            }
            new CartItemMultiDiscountPricer(currentTier, mdCartItem.getMultiDiscount())
                    .quote(context, effectiveSortedMdCartItem);

            prevItem = effectiveSortedMdCartItem;
        }
    }

    protected abstract boolean shouldAdvanceTier(CartItem prevItem, CartItem nextItem);

    /**
     * How tiers current items have
     * 
     * @param effectiveSortedMdCartItems
     * @return
     */
    protected abstract int countTiers(List<CartItem> effectiveSortedMdCartItems);

    /**
     * Filter and sort cart items by different algorithm
     *
     * @return
     */
    private List<CartItem> buildEffectiveSortedMdCartItems() {
        MultiDiscountAlgorithm algorithm = mdCartItem.getMultiDiscount().getAlgorithm();
        switch (algorithm) {
            case LEAST_EXPENSIVE:
                Function<List<CartItem>, CartItem> cartItemSelector = items -> sortByTotalPrice(items, true).get(0);
                return new EffectivePersonBasedMdCartItemBuilder(mdCartItem, cartItemSelector).build();
            case MOST_EXPENSIVE:
                cartItemSelector = items -> sortByTotalPrice(items, false).get(0);
                return new EffectivePersonBasedMdCartItemBuilder(mdCartItem, cartItemSelector).build();
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
        Map<String, List<CartItem>> itemsByIdentifier = itemsByPersonIdentifier(getMdCartItem().getCartItems());
        List<List<CartItem>> cartItemss = new ArrayList<>(itemsByIdentifier.values());
        Collections.sort(cartItemss, getAllProductComparator());

        return cartItemss.stream().flatMap(List::stream).collect(toList());
    }

    /**
     * Comparator first by CartItem net amounts, then by db index
     *
     * @return
     */
    protected static Comparator<List<CartItem>> totalPriceThenDbIndex() {
        return Comparator.comparing(Cart::getNetAmounts) // by total price
                .thenComparing(items -> { // by min db index
                    List<Long> itemIds = items.stream().map(CartItem::getId).collect(toList());
                    Collections.sort(itemIds);

                    return itemIds.get(0);
                });
    }

    @Override
    public void setQuoteContext(CartQuoteContext context) {
        this.context = context;
    }

    /**
     * Different multi discount has different comparator.
     *
     * @return
     */
    protected abstract Comparator<List<CartItem>> getAllProductComparator();

    /**
     * Sort by total price, then by db id.
     *
     * @param cartItems
     * @return
     */
    private List<CartItem> sortByTotalPrice(List<CartItem> cartItems, boolean asc) {
        Comparator<CartItem> comparator = Comparator.comparing(CartItem::getNetAmount);
        if (!asc) {
            comparator = comparator.reversed();
        }
        comparator = comparator.thenComparing(CartItem::getId);

        Collections.sort(cartItems, comparator);

        return cartItems;
    }
}
