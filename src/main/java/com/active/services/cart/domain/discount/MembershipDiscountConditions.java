package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.Cart;

public class MembershipDiscountConditions {
    public static DiscountCondition create(Long membershipId, String person, Cart cart) {
        return DiscountSequentialCondition.allOf(
                new MembershipDiscountCondition(membershipId, person, new MembershipMetadata()),
                new MembershipDiscountPurchaseRelatedMembershipProductCondition(membershipId, person, cart, null,
                        null));
    }
}
