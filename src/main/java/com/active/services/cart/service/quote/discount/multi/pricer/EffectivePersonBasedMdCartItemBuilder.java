package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;

@RequiredArgsConstructor
@Slf4j
public class EffectivePersonBasedMdCartItemBuilder implements Builder<List<CartItem>> {
    private final MultiDiscountCartItem mdCartItem;

    private final Function<List<CartItem>, CartItem> cartItemSelector;

    @Override
    public final List<CartItem> build() {
        List<CartItem> results = new ArrayList<>();

        Map<String, List<CartItem>> itemsByPersonIdentifier = mdCartItem.getCartItems().stream()
                .collect(groupingBy(CartItem::getPersonIdentifier));
        itemsByPersonIdentifier.forEach((personIdentifier, items) ->
            results.add(selectPersonItems(personIdentifier, items)));

        return results;
    }

    protected CartItem selectPersonItems(String personIdentifier, List<CartItem> items) {
        CartItem selectedItem = cartItemSelector.apply(items);
        if (items.size() > 1) {
            LOG.debug("Ignore following cart items for person: {}", personIdentifier);
            items.forEach(item -> {
                if (!item.equals(selectedItem)) {
                    LOG.debug(item.getIdentifier().toString());
                }
            });
        }

        return selectedItem;
    }
}
