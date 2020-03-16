package com.active.services.cart.service.quote.discount.membership2;

import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.ProductMembership;
import com.active.services.product.api.omsOnly.types.FindLatestMembershipDiscountsByProductIdsRsp;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Data
public class MembershipDiscountContext {
    private List<FindLatestMembershipDiscountsByProductIdsRsp> membershipDiscounts;

    private List<ProductMembership> productMemberships;

    public List<MembershipDiscountsHistory> getMembershipDiscountsHistory(Long productId) {
        return emptyIfNull(membershipDiscounts).stream().filter(rsp -> rsp.getProductId().equals(productId))
            .map(FindLatestMembershipDiscountsByProductIdsRsp::getHistories)
                .flatMap(List::stream).collect(Collectors.toList());
    }

    public List<Long> getNewItemMembershipIds() {
        return emptyIfNull(productMemberships).stream().map(ProductMembership::getMembershipId).collect(Collectors.toList());
    }
}
