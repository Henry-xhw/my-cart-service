package com.active.services.cart.domain;

import com.active.services.cart.model.CurrencyCode;
import com.active.services.cart.service.CartStatus;

import lombok.Data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Data
public class Cart extends BaseDomainObject {

    private UUID ownerId;

    private UUID keyerId;

    private CurrencyCode currencyCode;

    private int version;

    private int priceVersion;

    private boolean lock;

    private CartStatus cartStatus;

    private List<CartItem> items = new ArrayList<>();

    public Optional<CartItem> findCartItem(UUID cartItemId) {
        return Optional.ofNullable(findInItems(items, cartItemId));
    }

    private CartItem findInItems(List<CartItem> items, UUID cartItemId) {
        CartItem found = items.stream()
                .filter(it -> Objects.equals(it.getIdentifier(), cartItemId))
                .findAny()
                .orElse(null);
        if (found != null) {
            return found;
        }
        for (CartItem it : items) {
            found = findInItems(it.getSubItems(), cartItemId);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    public List<CartItem> getFlattenCartItems() {
        Queue<CartItem> q = new LinkedList<>(items);
        List<CartItem> flatten = new LinkedList<>();
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                CartItem it = q.poll();
                if (it != null) {
                    flatten.add(it);
                    emptyIfNull(it.getSubItems()).stream()
                            .filter(Objects::nonNull)
                            .forEach(q::offer);
                }
            }
        }
        return flatten;
    }
}