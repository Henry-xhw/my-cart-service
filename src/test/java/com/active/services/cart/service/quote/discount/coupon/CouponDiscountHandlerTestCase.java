package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountApplication;
import com.active.services.cart.service.quote.discount.DiscountConvertor;
import com.active.services.cart.service.quote.discount.DiscountFactory;
import com.active.services.cart.service.quote.discount.algorithm.BestDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.StackableFlatFirstDiscountAlgorithm;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.Product;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CouponDiscountHandlerTestCase {

    /**
     * filterDiscounts only return not expired discounts.
     */
    @Test
    public void filterDiscountsWithExpiredCheck() {

        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        List<DiscountApplication> discountApplicationList = new ArrayList<>();
        discountApplicationList.add(DiscountFactory.getDiscountApplication(AmountType.FLAT, new BigDecimal("10.00"), "code",
                DiscountAlgorithm.MOST_EXPENSIVE, null, null,
                cartQuoteContext));
        discountApplicationList.add(DiscountFactory.getDiscountApplication(AmountType.FIXED_AMOUNT, new BigDecimal("8.00"), "code",
                null,
                new DateTime(LocalDateTime.now().plusDays(1)), new DateTime(LocalDateTime.now().plusDays(2)),
                cartQuoteContext));

        CartItemDiscounts cartItemDiscounts = CartItemDiscounts.builder()
                .cartItem(cart.getFlattenCartItems().get(0))
                .discounts(discountApplicationList).build();

        CouponDiscountHandler handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);

        List<DiscountApplication> result = handler.filterDiscounts();
        assertTrue(result.contains(discountApplicationList.get(0)));
        assertFalse(result.contains(discountApplicationList.get(1)));
    }

    /**
     * filterDiscounts will return cart level discounts when
     * a. cart item CouponMode is HIGH_PRIORITY
     * b. cart item product with DiscountModel.NON_COMBINABLE_MINIMIZE_PRICE
     * c. cart item with effective discounts.
     */
    @Test
    public void filterDiscounts() {

        Cart cart = CartDataFactory.cart();

        String code1 = "code 1";
        String code2 = "code 2";
        String code3 = "code 3";
        cart.setCouponCodes(new HashSet<>(Arrays.asList(code1, code2)));

        CartItem cartItem = cart.getFlattenCartItems().get(0);
        cartItem.setCouponCodes(new HashSet<>(Arrays.asList(code2, code3)));

        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        List<DiscountApplication> discountApplicationList = new ArrayList<>();
        discountApplicationList.add(DiscountFactory.getDiscountApplication(AmountType.FLAT, new BigDecimal("10.00"), code1,
                DiscountAlgorithm.MOST_EXPENSIVE, null, null,
                cartQuoteContext));
        discountApplicationList.add(DiscountFactory.getDiscountApplication(AmountType.PERCENT, new BigDecimal("32.00"), code3,
                DiscountAlgorithm.MOST_EXPENSIVE,
                new DateTime(LocalDateTime.now().minusDays(1)), new DateTime(LocalDateTime.now().plusDays(1)),
                cartQuoteContext));

        CartItemDiscounts cartItemDiscounts = CartItemDiscounts.builder()
                .cartItem(cartItem)
                .discounts(discountApplicationList).build();

        cartItem.setCouponMode(CouponMode.HIGH_PRIORITY);
        cartQuoteContext.setProducts(Collections.singletonList(
                getProduct(cartItem.getProductId(), DiscountModel.COMBINABLE_FLAT_FIRST)));
        CouponDiscountHandler handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);

        //return two satisfy discounts when DiscountModel is COMBINABLE_FLAT_FIRST
        List<DiscountApplication> result = handler.filterDiscounts();
        assertTrue(result.contains(discountApplicationList.get(0)));
        assertTrue(result.contains(discountApplicationList.get(1)));

        cartQuoteContext.setProducts(Collections.singletonList(getProduct(cartItem.getProductId(),
                DiscountModel.NON_COMBINABLE_MINIMIZE_PRICE)));

        handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);

        //return cart level discounts when DiscountModel is NON_COMBINABLE_MINIMIZE_PRICE and CouponMode.HIGH_PRIORITY
        result = handler.filterDiscounts();
        assertFalse(result.contains(discountApplicationList.get(0)));
        assertTrue(result.contains(discountApplicationList.get(1)));

        cartItem.setCouponMode(null);
        handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);

        //return two satisfy discounts when CouponMode is null
        result = handler.filterDiscounts();
        assertTrue(result.contains(discountApplicationList.get(0)));
        assertTrue(result.contains(discountApplicationList.get(1)));

        cartItem.setCouponMode(CouponMode.HIGH_PRIORITY);
        discountApplicationList.remove(1);
        // will return cart leve discounts when there is no cart item level discount.
        handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);
        result = handler.filterDiscounts();
        assertTrue(result.contains(discountApplicationList.get(0)));
    }

    /**
     * filterDiscounts will check unique used when DiscountAlgorithm is MOST_EXPENSIVE.
     */
    @Test
    public void filterDiscountsCheckUniqueUsed() {

        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        CartItem cartItem = cart.getFlattenCartItems().get(0);
        Discount desc1 = DiscountFactory.getDiscount(AmountType.FLAT, new BigDecimal("11.00"), "code1",
                DiscountAlgorithm.MOST_EXPENSIVE, null, null);
        Discount desc2 = DiscountFactory.getDiscount(AmountType.PERCENT, new BigDecimal("10.00"), "code2", null, null, null);

        DiscountApplication discountApplication1 = DiscountConvertor.convert(desc1, cartQuoteContext);
        DiscountApplication discountApplication2 = DiscountConvertor.convert(desc2, cartQuoteContext);
        cartQuoteContext.setAppliedDiscounts(Arrays.asList(discountApplication1, discountApplication2));

        List<DiscountApplication> discountApplicationList = new ArrayList<>();
        discountApplicationList.add(DiscountConvertor.convert(desc1, cartQuoteContext));
        discountApplicationList.add(DiscountConvertor.convert(desc2, cartQuoteContext));

        CartItemDiscounts cartItemDiscounts = CartItemDiscounts.builder()
                .cartItem(cartItem)
                .discounts(discountApplicationList).build();

        cartQuoteContext.setProducts(Collections.singletonList(
                getProduct(cartItem.getProductId(), DiscountModel.COMBINABLE_FLAT_FIRST)));
        CouponDiscountHandler handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);

        // only return discountApplication2 with null DiscountAlgorithm.
        List<DiscountApplication> result = handler.filterDiscounts();
        assertFalse(result.contains(discountApplicationList.get(0)));
        assertTrue(result.contains(discountApplicationList.get(1)));
    }


    /**
     *
     * getDiscountAlgorithm return StackableFlatFirstDiscountAlgorithm when DiscountModel is COMBINABLE_FLAT_FIRST,
     * else, return BestDiscountAlgorithm.
     *
     */
    @Test
    public void getDiscountAlgorithm() {

        Cart cart = CartDataFactory.cart();

        CartItem cartItem = cart.getFlattenCartItems().get(0);

        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        cartQuoteContext.setProducts(Collections.singletonList(
                getProduct(cartItem.getProductId(), DiscountModel.NON_COMBINABLE_MINIMIZE_PRICE)));

        List<DiscountApplication> discountApplicationList = new ArrayList<>();
        discountApplicationList.add(DiscountFactory.getDiscountApplication(AmountType.FLAT, new BigDecimal("10.00"), "code",
                DiscountAlgorithm.MOST_EXPENSIVE,
                new DateTime(LocalDateTime.now().minusHours(1)), new DateTime(LocalDateTime.now().plusHours(1)),
                cartQuoteContext));
        CartItemDiscounts cartItemDiscounts = CartItemDiscounts.builder()
                .cartItem(cart.getFlattenCartItems().get(0))
                .discounts(discountApplicationList).build();

        CouponDiscountHandler handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);
        com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm algorithm =
                handler.getDiscountAlgorithm();
        assertTrue(algorithm instanceof BestDiscountAlgorithm);

        cartQuoteContext.setProducts(Collections.singletonList(
                getProduct(cartItem.getProductId(), DiscountModel.COMBINABLE_FLAT_FIRST)));

        handler = new CouponDiscountHandler(cartQuoteContext, cartItemDiscounts);
        algorithm = handler.getDiscountAlgorithm();
        assertTrue(algorithm instanceof StackableFlatFirstDiscountAlgorithm);
    }

    private Product getProduct(Long prodId, DiscountModel model) {
        Product product = new Product();
        product.setId(prodId);
        product.setDiscountModel(model);
        return product;
    }

}
