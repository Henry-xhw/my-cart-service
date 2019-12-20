package com.active.services.cart.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.active.services.cart.model.CurrencyCode;
import com.active.services.cart.service.CartStatus;
import lombok.Data;

@Data
public class Cart extends BaseDomainObject {

    private UUID ownerId;

    private UUID keyerId;

    private CurrencyCode currencyCode;

    private int version;

    private int priceVersion;

    private boolean isLock;

    private CartStatus cartStatus;

    private List<CartItem> items = new ArrayList<>();

    private List<MultiCartItemFee> fees = new ArrayList<>();


    public Optional<CartItem> findCartItem(UUID cartItemId) {
        return findCartItem(items, cartItemId);
    }

    private Optional<CartItem> findCartItem(List<CartItem> items, UUID cartItemId) {
        return items.stream().filter(Objects::nonNull)
            .filter(cartItem -> Objects.equals(cartItemId, cartItem.getIdentifier())).findFirst();
    }
}