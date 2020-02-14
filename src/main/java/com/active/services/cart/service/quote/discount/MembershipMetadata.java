package com.active.services.cart.service.quote.discount;

import lombok.Data;

import java.util.List;

@Data
public class MembershipMetadata {
    private List<MembershipMetadataDetail> details;

    @Data
    public static class MembershipMetadataDetail {
        private String person;
        private List<Long> memberships;
    }
}
