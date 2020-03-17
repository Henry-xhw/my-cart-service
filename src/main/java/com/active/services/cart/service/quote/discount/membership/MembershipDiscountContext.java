package com.active.services.cart.service.quote.discount.membership;

import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.ProductMembership;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Data
class MembershipDiscountContext {

    private Map<Long, List<MembershipDiscountsHistory>> productMembershipDiscounts;
    private List<ProductMembership> productMemberships;

    public List<MembershipDiscountsHistory> getMembershipDiscountsHistory(Long productId) {
        return productMembershipDiscounts.get(productId);
    }

    public List<Long> getNewItemMembershipIds() {
        return emptyIfNull(productMemberships).stream().map(ProductMembership::getMembershipId).collect(Collectors.toList());
    }
}
