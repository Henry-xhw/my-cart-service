package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.domain.CartItem;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MembershipSpecTestCase {

    private Long requiredMembershipId = RandomUtils.nextLong();

    @Test
    public void testSatisfy() {
        CartItem cartItem = new CartItem();
        cartItem.setMembershipIds(Collections.singleton(requiredMembershipId));

        List<Long> newItemMembershipIds = null;
        MembershipSpec membershipSpec = new MembershipSpec(newItemMembershipIds, cartItem, requiredMembershipId);
        assertThat(membershipSpec.satisfy()).isEqualTo(true);

        cartItem.setMembershipIds(Collections.singleton(requiredMembershipId + 1));
        membershipSpec = new MembershipSpec(newItemMembershipIds, cartItem, requiredMembershipId);
        assertThat(membershipSpec.satisfy()).isEqualTo(false);

        cartItem.setMembershipIds(Collections.singleton(requiredMembershipId + 1));
        newItemMembershipIds = Arrays.asList(requiredMembershipId + 2);
        membershipSpec = new MembershipSpec(newItemMembershipIds, cartItem, requiredMembershipId);
        assertThat(membershipSpec.satisfy()).isEqualTo(false);

        cartItem.setMembershipIds(Collections.singleton(requiredMembershipId));
        newItemMembershipIds = Arrays.asList(requiredMembershipId + 1, requiredMembershipId);
        membershipSpec = new MembershipSpec(newItemMembershipIds, cartItem, requiredMembershipId);
        assertThat(membershipSpec.satisfy()).isEqualTo(true);
    }
}
