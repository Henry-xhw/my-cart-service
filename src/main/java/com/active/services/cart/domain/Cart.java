package com.active.services.cart.domain;

import com.active.services.cart.service.CartStatus;
import com.active.services.cart.util.TreeBuilder;

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

    private List<Discount> discounts = new ArrayList<>();

    private Set<String> couponCodes;

    public Optional<CartItem> findCartItem(UUID cartItemId) {
        return getFlattenCartItems().stream()
            .filter(it -> Objects.equals(it.getIdentifier(), cartItemId))
            .findAny();
    }

    public List<CartItem> getFlattenCartItems() {
        return flattenCartItems(items);
    }

    public Cart setUnflattenItems(List<CartItem> flattenCartItems) {
        TreeBuilder<CartItem> baseTreeTreeBuilder = new TreeBuilder<>(flattenCartItems);
        items = baseTreeTreeBuilder.buildTree();
        return this;
    }

    public Cart unflattenItems() {
        TreeBuilder<CartItem> baseTreeTreeBuilder = new TreeBuilder<>(items);
        items = baseTreeTreeBuilder.buildTree();
        return this;
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