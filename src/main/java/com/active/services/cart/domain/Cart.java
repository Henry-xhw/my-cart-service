package com.active.services.cart.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import com.active.services.cart.model.CurrencyCode;

import lombok.Data;
import org.springframework.util.ObjectUtils;

@Data
public class Cart extends BaseDomainObject {

    private UUID ownerId;

    private UUID keyerId;

    private CurrencyCode currencyCode;

    private List<CartItem> items = new ArrayList<>();

    private List<CartItemShareFee> fees = new ArrayList<>();


    public Optional<CartItem> findCartItem(UUID cartItemId) {
        return findCartItem(items, cartItemId);
    }

    private static Optional<CartItem> findCartItem(List<CartItem> items, UUID cartItemId) {
        return Optional.ofNullable(items).map(cartItems -> cartItems.stream()
            .filter(cartItem -> ObjectUtils.nullSafeEquals(cartItemId, cartItem.getIdentifier())).findFirst())
            .orElseGet(Optional::empty);
    }
}