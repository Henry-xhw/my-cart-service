package com.active.services.cart.domain.discount;

import com.active.services.ProductType;
import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.infrastructure.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class NonMembershipCartItemSelector implements CartItemSelector {
    private final CartItemSelector flattenSelector;
    private final ProductRepository productRepo;

    @Override
    public List<CartItem> select(Cart cart) {
        return flattenSelector.select(cart).stream()
                .filter(item -> productRepo.getProduct(item.getProductId())
                        .filter(p -> p.getProductType() != ProductType.MEMBERSHIP)
                        .isPresent())
                .collect(Collectors.toList());
    }
}
