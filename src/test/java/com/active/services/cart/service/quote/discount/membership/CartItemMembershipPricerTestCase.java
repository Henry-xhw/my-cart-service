package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartItemMembershipPricerTestCase {

    private CartItemMembershipPricer cartItemMembershipPricer;

    private CartItem cartItem = mock(CartItem.class);

    private CartQuoteContext mockQuoteContext = mock(CartQuoteContext.class);

    private MembershipDiscountContext mockMembershipContext = mock(MembershipDiscountContext.class);

    @Before
    public void setUp() {
        cartItemMembershipPricer = new CartItemMembershipPricer(mockMembershipContext);
    }

    @Test
    public void testDoQuoteNoMembershipDiscountHistory() {
        cartItemMembershipPricer.doQuote(mockQuoteContext, cartItem);
    }

    @Test
    public void testDoQuoteNoMatchDiscounts() {
        Long productId = 1L;
        when(cartItem.getProductId()).thenReturn(productId);

        Cart cart = mock(Cart.class);
        when(mockQuoteContext.getCart()).thenReturn(cart);

        MembershipDiscountsHistory membershipDiscountHistory = mock(MembershipDiscountsHistory.class);
        when(membershipDiscountHistory.getMembershipId()).thenReturn(12345L);

        Currency currency = Currency.getInstance("USD");
        when(mockQuoteContext.getCurrency()).thenReturn(currency);

        List<MembershipDiscountsHistory> membershipDiscountsHistories = Arrays.asList(membershipDiscountHistory);
        when(mockMembershipContext.getMembershipDiscountsHistory(productId)).thenReturn(membershipDiscountsHistories);

        cartItemMembershipPricer.doQuote(mockQuoteContext, cartItem);
    }

    @Test
    public void testDoQuoteMatchedDiscounts() {
        Long productId = 1L;
        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getProductId()).thenReturn(productId);

        Cart cart = mock(Cart.class);
        when(mockQuoteContext.getCart()).thenReturn(cart);
        MembershipDiscountsHistory membershipDiscountHistory = mock(MembershipDiscountsHistory.class);

        Currency currency = Currency.getInstance("USD");
        when(mockQuoteContext.getCurrency()).thenReturn(currency);

        List<MembershipDiscountsHistory> membershipDiscountsHistories = Arrays.asList(membershipDiscountHistory);
        when(mockMembershipContext.getMembershipDiscountsHistory(productId)).thenReturn(membershipDiscountsHistories);

        cartItemMembershipPricer.doQuote(mockQuoteContext, cartItem);
    }
}
