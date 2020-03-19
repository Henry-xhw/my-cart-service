package com.active.services.cart.service.quote.discount.membership;

import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.ProductMembership;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class MembershipDiscountContextTestCase {

    private MembershipDiscountContext membershipDiscountContext;

    @Before
    public void setUp() {
        membershipDiscountContext = new MembershipDiscountContext();
    }

    @Test
    public void testGetMembershipDiscountsHistory() {
        Long productId = 1L;
        Map<Long, List<MembershipDiscountsHistory>> productMembershipDiscounts = new HashMap<>();
        membershipDiscountContext.setProductMembershipDiscounts(productMembershipDiscounts);
        assertThat(membershipDiscountContext.getMembershipDiscountsHistory(productId)).isNull();

        productMembershipDiscounts.put(productId, Arrays.asList());
        assertThat(membershipDiscountContext.getMembershipDiscountsHistory(productId)).hasSize(0);

        productMembershipDiscounts.put(productId, Arrays.asList(new MembershipDiscountsHistory()));
        assertThat(membershipDiscountContext.getMembershipDiscountsHistory(productId)).hasSize(1);
    }

    @Test
    public void testGetNewItemMembershipIds() {
        assertThat(membershipDiscountContext.getNewItemMembershipIds()).hasSize(0);

        ProductMembership productMembership = new ProductMembership();
        productMembership.setMembershipId(1L);
        membershipDiscountContext.setProductMemberships(Arrays.asList(productMembership));
        assertThat(membershipDiscountContext.getNewItemMembershipIds()).hasSize(1);
    }
}
