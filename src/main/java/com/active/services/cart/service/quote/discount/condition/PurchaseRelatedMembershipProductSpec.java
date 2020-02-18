package com.active.services.cart.service.quote.discount.condition;

import com.active.services.cart.application.CartItemSelector;
import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PurchaseRelatedMembershipProductSpec implements DiscountSpecification {
    private final Long membershipId;
    private final String person;
    private final Cart cart;
    private final CartItemSelector flattenCartItemSelector;
    private final ProductServiceSoap productRepo;

    public boolean satisfy() {
        List<Long> productIds = flattenCartItemSelector.select(cart).stream()
                .filter(item -> person == null && item.getPersonIdentifier() == null ||
                        item.getPersonIdentifier().equalsIgnoreCase(person))
                .map(CartItem::getProductId)
                .collect(Collectors.toList());
        return productRepo.findProductMemberships(productIds).stream()
                .anyMatch(pm -> pm.getMembershipId().equals(membershipId));
    }
}
