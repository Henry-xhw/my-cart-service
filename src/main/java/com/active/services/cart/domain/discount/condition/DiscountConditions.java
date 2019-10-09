package com.active.services.cart.domain.discount.condition;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.discount.DiscountCondition;
import com.active.services.cart.domain.discount.MembershipMetadata;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;

public class DiscountConditions {
    public static DiscountCondition membershipDiscount(Long membershipId, String person, Cart cart,
                                                       DateTime evaluateDt, MembershipDiscountsHistory md) {
        return DiscountSequentialCondition.allOf(
                new NotExpiredCondition(md.getStartDate(), md.getEndDate(), evaluateDt),
                new MembershipInMetadataCondition(membershipId, person, new MembershipMetadata()),
                new PurchaseRelatedMembershipProductCondition(membershipId, person, cart, null, null));
    }
}
