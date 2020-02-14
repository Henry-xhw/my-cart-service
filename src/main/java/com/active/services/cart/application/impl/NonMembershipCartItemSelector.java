package com.active.services.cart.application.impl;

import com.active.services.ProductType;
import com.active.services.cart.application.CartItemSelector;
import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NonMembershipCartItemSelector implements CartItemSelector {
    private final CartItemSelector flattenCartItemSelector;
    private final ProductServiceSoap productRepo;

    @Override
    public List<CartItem> select(Cart cart) {
        return flattenCartItemSelector.select(cart).stream()
                .filter(item -> productRepo.getProduct(item.getProductId())
                        .filter(p -> p.getProductType() != ProductType.MEMBERSHIP)
                        .isPresent())
                .collect(Collectors.toList());
    }
}
