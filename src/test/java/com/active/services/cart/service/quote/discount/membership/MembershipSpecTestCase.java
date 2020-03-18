package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.domain.CartItem;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MembershipSpecTestCase {

    @Test
    public void testSatisfy() {
        CartItem cartItem = new CartItem();
        cartItem.setMembershipId(12345L);
        Long requiredMembershipId = 12345L;
        List<Long> newItemMembershipIds = null;

        MembershipSpec membershipSpec = new MembershipSpec(newItemMembershipIds, cartItem, requiredMembershipId);
        assertThat(membershipSpec.satisfy()).isEqualTo(true);

        cartItem.setMembershipId(1L);
        membershipSpec = new MembershipSpec(newItemMembershipIds, cartItem, requiredMembershipId);
        assertThat(membershipSpec.satisfy()).isEqualTo(false);

        cartItem.setMembershipId(1L);
        newItemMembershipIds = Arrays.asList(2L);
        membershipSpec = new MembershipSpec(newItemMembershipIds, cartItem, requiredMembershipId);
        assertThat(membershipSpec.satisfy()).isEqualTo(false);

        cartItem.setMembershipId(1L);
        newItemMembershipIds = Arrays.asList(2L, 12345L);
        membershipSpec = new MembershipSpec(newItemMembershipIds, cartItem, requiredMembershipId);
        assertThat(membershipSpec.satisfy()).isEqualTo(true);
    }
}
