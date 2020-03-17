package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.coupon.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountFactory;
import com.active.services.cart.service.quote.discount.coupon3.CouponDiscountHandler;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CartItemDiscountPricerTestCase {

    @Test
    public void quoteWithoutDiscount() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        CartItemDiscounts cartItemDiscounts = CartItemDiscounts.builder()
                .cartItem(cart.getFlattenCartItems().get(0))
                .discounts(new ArrayList<>()).build();

        CouponDiscountHandler handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts, new ArrayList<>());
        CartItemDiscountPricer cartItemDiscountPricer = new CartItemDiscountPricer(handler);
        cartItemDiscountPricer.quote(cartQuoteContext, cart.getFlattenCartItems().get(0));
        checkDiscountAmount(cart.getFlattenCartItems().get(0), null);
    }

    @Test
    public void quote() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        List<Discount> discountApplicationList = new ArrayList<>();
        discountApplicationList.add(DiscountFactory.getCouponCodeDiscountApplication(AmountType.FLAT,
                new BigDecimal("2.00"), "code",
                DiscountAlgorithm.MOST_EXPENSIVE,
                new DateTime(LocalDateTime.now().minusDays(1)), new DateTime(LocalDateTime.now().plusDays(1)),
                cartQuoteContext));

        CartItemDiscounts cartItemDiscounts = CartItemDiscounts.builder()
                .cartItem(cart.getFlattenCartItems().get(0))
                .discounts(discountApplicationList).build();
        List<DiscountUsage> discountUsages = getDiscountUsageList(discountApplicationList);

        CouponDiscountHandler handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts, discountUsages);
        CartItemDiscountPricer cartItemDiscountPricer = new CartItemDiscountPricer(handler);
        cartItemDiscountPricer.quote(cartQuoteContext, cart.getFlattenCartItems().get(0));
        checkDiscountAmount(cart.getFlattenCartItems().get(0), new BigDecimal("2.00"));
        assertEquals(new BigDecimal("8.00"), cart.getFlattenCartItems().get(0).getNetPrice());
    }



    private List<DiscountUsage> getDiscountUsageList(List<com.active.services.cart.domain.Discount> discountApplicationList) {
        return discountApplicationList.stream().map(discount -> getDiscountUsage(discount.getDiscountId(),
                2, 1)).collect(Collectors.toList());
    }

    private DiscountUsage getDiscountUsage(Long discountId, Integer limit, Integer usage) {
        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscountId(discountId);
        discountUsage.setLimit(limit);
        discountUsage.setUsage(usage);
        return discountUsage;
    }

    private void checkDiscountAmount(CartItem cartItem, BigDecimal amount) {
        Optional<BigDecimal> discAmount =
                cartItem.getPriceCartItemFee().get().getSubItems().stream()
                        .filter(cartItemFee -> CartItemFeeType.isPriceDiscount(cartItemFee.getType()))
                        .map(CartItemFee::getUnitPrice).findFirst();
        if (discAmount.isPresent() && amount != null) {
            assertTrue(discAmount.get().compareTo(amount) == 0);
        } else {
            assertTrue(amount == null && !discAmount.isPresent());
        }
    }
}
