package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountType;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartItemMembershipPricerTestCase {

    @Mock
    private MembershipDiscountContext mockMembershipContext;

    @InjectMocks
    private CartItemMembershipPricer cartItemMembershipPricer;

    private Long productId = RandomUtils.nextLong();

    private Long membershipId = RandomUtils.nextLong();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoQuoteNoMembershipDiscountHistory() {
        CartQuoteContext context = buildCartQuoteContext();
        CartItem cartItem = context.getCart().getItems().get(0);
        cartItemMembershipPricer.doQuote(context, cartItem);
        assertThat(context.getAppliedDiscounts()).isEmpty();
    }

    @Test
    public void testDoQuoteNoMatchDiscounts() {
        CartQuoteContext context = buildCartQuoteContext();

        MembershipDiscountsHistory membershipDiscountHistory = mock(MembershipDiscountsHistory.class);
        when(membershipDiscountHistory.getMembershipId()).thenReturn(membershipId);

        List<MembershipDiscountsHistory> membershipDiscountsHistories = Arrays.asList(membershipDiscountHistory);
        when(mockMembershipContext.getMembershipDiscountsHistory(productId)).thenReturn(membershipDiscountsHistories);

        CartItem cartItem = context.getCart().getItems().get(0);
        cartItemMembershipPricer.doQuote(context, cartItem);
        assertThat(context.getAppliedDiscounts()).isEmpty();
    }

    @Test
    public void testDoQuoteMatchedDiscounts() {
        CartQuoteContext context = buildCartQuoteContext();

        BigDecimal discountAmt = new BigDecimal("2.00");
        MembershipDiscountsHistory membershipDiscountHistory = mock(MembershipDiscountsHistory.class);
        when(membershipDiscountHistory.getMembershipId()).thenReturn(membershipId);
        when(membershipDiscountHistory.getAmountType()).thenReturn(AmountType.FLAT);
        when(membershipDiscountHistory.getAmount()).thenReturn(discountAmt);

        List<MembershipDiscountsHistory> membershipDiscountsHistories = Arrays.asList(membershipDiscountHistory);
        when(mockMembershipContext.getMembershipDiscountsHistory(productId)).thenReturn(membershipDiscountsHistories);

        CartItem cartItem = context.getCart().getItems().get(0);
        cartItem.setMembershipIds(Collections.singleton(membershipId));
        cartItemMembershipPricer.doQuote(context, cartItem);

        Predicate<Discount> membershipPredicate = d -> d.getDiscountType() == DiscountType.MEMBERSHIP &&
            d.getMembershipId().equals(membershipId);

        assertThat(context.getAppliedDiscounts()).hasSizeGreaterThan(0).anyMatch(membershipPredicate);
    }

    private CartQuoteContext buildCartQuoteContext() {
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setProductId(productId);

        Cart cart = CartDataFactory.cart();
        cart.setItems(Arrays.asList(cartItem));

        CartQuoteContext context = new CartQuoteContext(cart);
        return context;
    }

}
