package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.multi.CartItemMultiDiscountPricer;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;
import com.active.services.product.discount.multi.SortedTierListBuilder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import static com.active.services.oms.BdUtil.comparesToZero;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Getter
public abstract class MultiDiscountBasePricer implements MultiDiscountPricer {
    private final MultiDiscountCartItem mdCartItem;

    private final MultiDiscountThresholdSetting effectiveThresholdSetting;

    @Override
    public final void price() {
        // All items does not have net price.
        if (mdCartItem.getCartItems().stream().allMatch(item -> comparesToZero(item.getNetPrice()))) {
            return;
        }

        // Step1: build effective cart items by different algorithm.
        PersonBasedEffectiveMdCartItemBuilder builder = new PersonBasedEffectiveMdCartItemBuilder(mdCartItem);
        builder.allProductsComparator(getAllProductComparator());
        List<CartItem> effectiveSortedMdCartItems = builder.build();

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
                    .quote(CartQuoteContext.get(), effectiveSortedMdCartItem);

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

    /**
     * Different multi discount has different comparator.
     *
     * @return
     */
    protected abstract Comparator<List<CartItem>> getAllProductComparator();
}
