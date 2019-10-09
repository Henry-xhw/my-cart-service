package com.active.services.cart.domain.discount;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

@RequiredArgsConstructor
public class MembershipInMetadataCondition implements DiscountCondition {
    private final Long membershipId;
    private final String person;
    private final MembershipMetadata membershipMetadata;

    public boolean satisfy() {
        if (membershipMetadata == null || CollectionUtils.isEmpty(membershipMetadata.getDetails())) {
            return false;
        }

        return membershipMetadata.getDetails().stream()
                .anyMatch(m -> m.getPerson().equalsIgnoreCase(person) &&
                        m.getMemberships().contains(membershipId));
    }
}
