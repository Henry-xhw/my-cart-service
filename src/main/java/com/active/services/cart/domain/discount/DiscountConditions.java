package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.Cart;

public class DiscountConditions {
    public static DiscountCondition membershipDiscountConditions(Long membershipId, String person, Cart cart) {
        return DiscountSequentialCondition.allOf(
                new MembershipInMetadataCondition(membershipId, person, new MembershipMetadata()),
                new PurchaseRelatedMembershipProductCondition(membershipId, person, cart, null, null));
    }
}
