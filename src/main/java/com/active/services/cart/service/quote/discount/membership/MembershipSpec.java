package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class MembershipSpec implements DiscountSpecification {
    private final List<Long> newItemMembershipIds;

    private final List<Long> quoteMembershipIds;
    @NonNull
    private final Long requiredMembershipId;

    @Override
    public boolean satisfy() {
        return (quoteMembershipIds != null && quoteMembershipIds.contains(requiredMembershipId)) ||
            (newItemMembershipIds != null && newItemMembershipIds.contains(requiredMembershipId));
    }
}
