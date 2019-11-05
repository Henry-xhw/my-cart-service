package com.active.services.cart.domain.discount.condition;

import com.active.services.cart.application.CartItemSelector;
import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.discount.MembershipMetadata;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscountSpecifications {
    private final CartItemSelector flattenCartItemSelector;
    private final ProductRepository productRepo;

    public DiscountSpecification membershipDiscount(Long membershipId, String person, Cart cart,
                                                           DateTime evaluateDt, MembershipDiscountsHistory md) {
        return DiscountSequentialSpecification.allOf(
                new NotExpiredSpecification(md.getStartDate(), md.getEndDate(), evaluateDt),
                DiscountOrSpecifications.anyOf(
                        new MembershipInMetadataSpecification(membershipId, person, new MembershipMetadata()),
                        new PurchaseRelatedMembershipProductSpecification(membershipId, person, cart, flattenCartItemSelector, productRepo))
                );
    }
}
