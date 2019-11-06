package com.active.services.cart.domain.discount.condition;

import com.active.services.cart.domain.discount.MembershipMetadata;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

@RequiredArgsConstructor
public class MembershipInMetadataSpecification implements DiscountSpecification {
    private final Long membershipId;
    private final String person;
    private final MembershipMetadata membershipMetadata;

    public boolean satisfy() {
        if (person == null || membershipMetadata == null || CollectionUtils.isEmpty(membershipMetadata.getDetails())) {
            return false;
        }

        return membershipMetadata.getDetails().stream()
                .anyMatch(m -> m.getPerson().equalsIgnoreCase(person) &&
                        m.getMemberships().contains(membershipId));
    }
}
