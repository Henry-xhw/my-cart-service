package com.active.services.cart.service.quote.discount.condition;

import com.active.services.cart.service.quote.discount.MembershipMetadata;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

@RequiredArgsConstructor
public class MembershipInMetadataSpec implements DiscountSpecification {
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
