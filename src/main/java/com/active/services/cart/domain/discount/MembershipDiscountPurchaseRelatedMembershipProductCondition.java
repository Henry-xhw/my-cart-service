package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.infrastructure.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MembershipDiscountPurchaseRelatedMembershipProductCondition implements DiscountCondition {
    private final Long membershipId;
    private final String person;
    private final Cart cart;
    private final CartItemSelector selector;
    private final ProductRepository productRepo;

    public boolean satisfy() {
        List<Long> productIds = selector.select(cart).stream()
                .filter(item -> item.getPersonIdentifier().equalsIgnoreCase(person))
                .map(CartItem::getProductId)
                .collect(Collectors.toList());
        return productRepo.findProductMemberships(productIds).stream()
                .anyMatch(pm -> pm.getMembershipId().equals(membershipId));
    }
}
