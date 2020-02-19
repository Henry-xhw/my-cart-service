package com.active.services.cart.domain;

import com.active.services.cart.service.CartStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class Cart extends BaseDomainObject {

    private UUID ownerId;

    private UUID keyerId;

    private String currencyCode;

    private int version;

    private int priceVersion;

    private boolean lock;

    private CartStatus cartStatus;

    private List<CartItem> items = new ArrayList<>();

    private UUID reservationId;

    private Set<String> couponCodes;

    public Optional<CartItem> findCartItem(UUID cartItemId) {
        return getFlattenCartItems().stream()
            .filter(it -> Objects.equals(it.getIdentifier(), cartItemId))
            .findAny();
    }

    public List<CartItem> getFlattenCartItems() {
        return flattenCartItems(items);
    }

    public static List<CartItem> flattenCartItems(List<CartItem> items) {
        Queue<CartItem> q = new LinkedList<>(items);
        List<CartItem> flatten = new LinkedList<>();
        while (!q.isEmpty()) {
            CartItem it = q.poll();
            if (it != null) {
                flatten.add(it);
                emptyIfNull(it.getSubItems()).stream()
                        .filter(Objects::nonNull)
                        .forEach(q::offer);
            }
        }
        return flatten;
    }
}