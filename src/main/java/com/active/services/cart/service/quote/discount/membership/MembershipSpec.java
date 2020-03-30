package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

@RequiredArgsConstructor
public class MembershipSpec implements DiscountSpecification {
    private final List<Long> newItemMembershipIds;
    @NonNull
    private final CartItem cartItem;
    @NonNull
    private final Long requiredMembershipId;

    @Override
    public boolean satisfy() {
        return (cartItem.getMembershipIds() != null && cartItem.getMembershipIds().contains(requiredMembershipId)) ||
            (CollectionUtils.isNotEmpty(newItemMembershipIds) && newItemMembershipIds.contains(requiredMembershipId));
    }
}
