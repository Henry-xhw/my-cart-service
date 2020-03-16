package com.active.services.cart.service.quote.discount.condition;

import com.active.services.cart.domain.CartItem;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
public class MemberShipSpec implements DiscountSpecification {
    private final List<Long> newItemMembershipIds;
    @NonNull
    private final CartItem cartItem;
    @NonNull
    private final Long requiredMembershipId;

    @Override
    public boolean satisfy() {
        return cartItem.getMembershipId() == requiredMembershipId || (CollectionUtils.isEmpty(newItemMembershipIds) &&
                newItemMembershipIds.contains(requiredMembershipId));
    }
}
