package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountApplication;
import com.active.services.cart.service.quote.discount.DiscountFactory;
import com.active.services.cart.service.quote.discount.coupon.CouponDiscountHandler;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountAlgorithm;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class CartItemDiscountPricerTestCase {

    @Test
    public void quoteWithoutDiscount() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        CartItemDiscounts cartItemDiscounts = CartItemDiscounts.builder()
                .cartItem(cart.getFlattenCartItems().get(0))
                .discounts(new ArrayList<>()).build();

        CouponDiscountHandler handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);
        CartItemDiscountPricer cartItemDiscountPricer = new CartItemDiscountPricer(handler);
        cartItemDiscountPricer.quote(cartQuoteContext, cart.getFlattenCartItems().get(0));
        checkDiscountAmount(cart.getFlattenCartItems().get(0), null);
    }

    @Test
    public void quoteWhenNetPriceIsZero() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        cart.getFlattenCartItems().get(0).setNetPrice(new BigDecimal(0));

        List<DiscountApplication> discountApplicationList = new ArrayList<>();
        discountApplicationList.add(DiscountFactory.getDiscountApplication(AmountType.FLAT,
                new BigDecimal("2.00"), "code",
                DiscountAlgorithm.MOST_EXPENSIVE,
                new DateTime(LocalDateTime.now().minusHours(1)), new DateTime(LocalDateTime.now().plusHours(1)),
                cartQuoteContext));

        CartItemDiscounts cartItemDiscounts = CartItemDiscounts.builder()
                .cartItem(cart.getFlattenCartItems().get(0))
                .discounts(discountApplicationList).build();

        CouponDiscountHandler handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);
        CartItemDiscountPricer cartItemDiscountPricer = new CartItemDiscountPricer(handler);
        cartItemDiscountPricer.quote(cartQuoteContext, cart.getFlattenCartItems().get(0));
        checkDiscountAmount(cart.getFlattenCartItems().get(0), null);
    }

    @Test
    public void quote() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        List<DiscountApplication> discountApplicationList = new ArrayList<>();
        discountApplicationList.add(DiscountFactory.getDiscountApplication(AmountType.FLAT,
                new BigDecimal("2.00"), "code",
                DiscountAlgorithm.MOST_EXPENSIVE,
                new DateTime(LocalDateTime.now().minusDays(1)), new DateTime(LocalDateTime.now().plusDays(1)),
                cartQuoteContext));

        CartItemDiscounts cartItemDiscounts = CartItemDiscounts.builder()
                .cartItem(cart.getFlattenCartItems().get(0))
                .discounts(discountApplicationList).build();

        CouponDiscountHandler handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);
        CartItemDiscountPricer cartItemDiscountPricer = new CartItemDiscountPricer(handler);
        cartItemDiscountPricer.quote(cartQuoteContext, cart.getFlattenCartItems().get(0));
        checkDiscountAmount(cart.getFlattenCartItems().get(0), new BigDecimal("2.00"));
        assertEquals(new BigDecimal("8.00"), cart.getFlattenCartItems().get(0).getNetPrice());
    }

    private void checkDiscountAmount(CartItem cartItem, BigDecimal amount) {
        Optional<BigDecimal> discAmount =
                cartItem.getPriceCartItemFee().get().getSubItems().stream()
                        .filter(cartItemFee -> cartItemFee.getType() == CartItemFeeType.DISCOUNT)
                        .map(CartItemFee::getUnitPrice).findFirst();
        assertEquals(Optional.ofNullable(amount), discAmount);
    }
}
