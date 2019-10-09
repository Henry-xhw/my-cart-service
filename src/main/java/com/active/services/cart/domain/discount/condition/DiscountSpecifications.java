package com.active.services.cart.domain.discount.condition;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.discount.MembershipMetadata;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;

public class DiscountSpecifications {
    public static DiscountSpecification membershipDiscount(Long membershipId, String person, Cart cart,
                                                           DateTime evaluateDt, MembershipDiscountsHistory md) {
        return DiscountSequentialSpecification.allOf(
                new NotExpiredSpecification(md.getStartDate(), md.getEndDate(), evaluateDt),
                new MembershipInMetadataSpecification(membershipId, person, new MembershipMetadata()),
                new PurchaseRelatedMembershipProductSpecification(membershipId, person, cart, null, null));
    }
}
