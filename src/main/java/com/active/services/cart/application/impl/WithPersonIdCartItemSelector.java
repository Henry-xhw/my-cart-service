package com.active.services.cart.application.impl;

import com.active.services.cart.application.CartItemSelector;
import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WithPersonIdCartItemSelector implements CartItemSelector {
    private final CartItemSelector flattenCartItemSelector;

    @Override
    public List<CartItem> select(Cart cart) {
        return flattenCartItemSelector.select(cart).stream()
                .filter(it -> Objects.nonNull(it.getPersonIdentifier()))
                .collect(Collectors.toList());
    }
}
