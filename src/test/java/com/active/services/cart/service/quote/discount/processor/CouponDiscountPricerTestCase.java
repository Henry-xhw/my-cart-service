package com.active.services.cart.service.quote.discount.processor;


import com.active.services.DiscountModel;
import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;
import com.active.services.product.Product;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.assertj.core.groups.Tuple;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CouponDiscountPricerTestCase {

    @MockBean
    private ProductServiceSoap productRepo;
   // @Autowired private DiscountSpecs discountSpecs;
    @Autowired
    private CouponDiscountPricer couponDiscountPricer;

    @Test
    public void quoteWithoutCouponCodes() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext context = new CartQuoteContext(cart);
        CartItem cartItem = cart.getFlattenCartItems().get(0);

        couponDiscountPricer.quote(context, cartItem);
        assertTrue(CollectionUtils.isEmpty(cartItem.getFees().get(0).getSubItems()));
    }

    @Test
    public void quoteWithUnmatchedCouponCodes() {
        Cart cart = CartDataFactory.cart();
        String code1 = "code 1";
        String code2 = "code 2";
        List<String> codes = Arrays.asList(code1, code2);

        cart.setCouponCodes(codes);
        CartQuoteContext context = new CartQuoteContext(cart);
        CartItem cartItem = cart.getFlattenCartItems().get(0);

        when(productRepo.findDiscountsByProductIdAndCode(eq(cartItem.getProductId()),
                eq(codes))).thenReturn(new ArrayList<>());
        couponDiscountPricer.quote(context, cartItem);
        assertTrue(CollectionUtils.isEmpty(cartItem.getFees().get(0).getSubItems()));
    }

    @Test
    public void quoteWithBothCartAndCartItemCouponCodePlatFirstAlgorithm() {

        Cart cart = CartDataFactory.cart();
        String code1 = "code 1";
        String code2 = "code 2";
        String code3 = "code 3";

        cart.setCouponCodes(Arrays.asList(code1, code2));

        CartQuoteContext context = new CartQuoteContext(cart);

        CartItem cartItem = cart.getFlattenCartItems().get(0);
        cartItem.setCouponCodes(Arrays.asList(code2, code3));
        List<String> codes = Arrays.asList(code1, code2, code3);

        List<Discount> discounts = new ArrayList<>();
        discounts.add(getDiscount(AmountType.FLAT, new BigDecimal("2.00"), code1));
        discounts.add(getDiscount(AmountType.PERCENT, new BigDecimal("10.00"), code2));
        discounts.add(getDiscount(AmountType.FIXED_AMOUNT, new BigDecimal("6.00"), code3));

        when(productRepo.findDiscountsByProductIdAndCode(eq(cartItem.getProductId()),
                eq(codes))).thenReturn(discounts);

        couponDiscountPricer.quote(context, cartItem);

        assertNotNull(cartItem.getFees().get(0).getSubItems());

        checkCartItemFee(cartItem.getFees().get(0).getSubItems(), 3,
                tuple(CartItemFeeType.DISCOUNT, FeeTransactionType.CREDIT, new BigDecimal("4.00")),
                tuple(CartItemFeeType.DISCOUNT, FeeTransactionType.CREDIT, new BigDecimal("2.00")),
                tuple(CartItemFeeType.DISCOUNT, FeeTransactionType.CREDIT, new BigDecimal("0.40"))
                );
    }


    @Test
    public void priceWithBestAlgorithm() {

        Cart cart = CartDataFactory.cart();
        String code1 = "code 1";
        String code2 = "code 2";
        String code3 = "code 3";

        cart.setCouponCodes(Arrays.asList(code1, code2));

        CartItem cartItem = cart.getFlattenCartItems().get(0);
        cartItem.setCouponMode(CouponMode.HIGH_PRIORITY);
        cartItem.setCouponCodes(Arrays.asList(code2, code3));
        List<String> codes = Arrays.asList(code1, code2, code3);

        List<Discount> discounts = new ArrayList<>();
        discounts.add(getDiscount(AmountType.FLAT, new BigDecimal("2.00"), code1));
        discounts.add(getDiscount(AmountType.PERCENT, new BigDecimal("10.00"), code2));
        discounts.add(getDiscount(AmountType.FIXED_AMOUNT, new BigDecimal("6.00"), code3));

        CartQuoteContext context = new CartQuoteContext(cart);

        context.setProducts(Collections.singletonList(getProduct(cartItem.getProductId(), DiscountModel.NON_COMBINABLE_MINIMIZE_PRICE)));

        when(productRepo.findDiscountsByProductIdAndCode(eq(cartItem.getProductId()),
                eq(codes))).thenReturn(discounts);

        couponDiscountPricer.quote(context, cartItem);

        assertNotNull(cartItem.getFees().get(0).getSubItems());

        checkCartItemFee(cartItem.getFees().get(0).getSubItems(), 1,
                tuple(CartItemFeeType.DISCOUNT, FeeTransactionType.CREDIT, new BigDecimal("4.00")));
    }

    @Test
    public void priceWithCheckedUsedOnceForMostExpensive() {

        Cart cart = CartDataFactory.cart();
        String code1 = "code 1";
        String code2 = "code 2";
        String code3 = "code 3";

        cart.setCouponCodes(Arrays.asList(code1, code2));

        CartItem cartItem = cart.getFlattenCartItems().get(0);
        cartItem.setCouponMode(CouponMode.HIGH_PRIORITY);
        cartItem.setCouponCodes(Arrays.asList(code2, code3));
        List<String> codes = Arrays.asList(code1, code2, code3);

        List<Discount> discounts = new ArrayList<>();
        discounts.add(getDiscount(AmountType.FLAT, new BigDecimal("2.00"), code1));
        discounts.add(getDiscount(AmountType.PERCENT, new BigDecimal("10.00"), code2));
        discounts.add(getDiscount(AmountType.FIXED_AMOUNT, new BigDecimal("6.00"), code3));

        CartQuoteContext context = new CartQuoteContext(cart);

        Discount discount3 = discounts.get(2);
        discount3.setDiscountAlgorithm(DiscountAlgorithm.MOST_EXPENSIVE);

        context.addAppliedDiscount(new com.active.services.cart.service.quote.discount.Discount(
                "name", "disc", discount3.getAmount(), discount3.getAmountType(),
                discount3.getId(), DiscountType.COUPON,
                discount3.getCouponCode(), discount3.getDiscountAlgorithm()
        ));

        context.setProducts(Collections.singletonList(getProduct(cartItem.getProductId(), DiscountModel.NON_COMBINABLE_MINIMIZE_PRICE)));

        when(productRepo.findDiscountsByProductIdAndCode(eq(cartItem.getProductId()),
                eq(codes))).thenReturn(discounts);

        couponDiscountPricer.quote(context, cartItem);

        assertNotNull(cartItem.getFees().get(0).getSubItems());

        checkCartItemFee(cartItem.getFees().get(0).getSubItems(), 1,
                tuple(CartItemFeeType.DISCOUNT, FeeTransactionType.CREDIT, new BigDecimal("1.00")));
    }

    private Product getProduct(Long prodId, DiscountModel model) {
        Product product = new Product();
        product.setId(prodId);
        product.setDiscountModel(model);
        return product;
    }


    private void checkCartItemFee(List<CartItemFee> chunks, Integer size, Tuple... tuples) {
        assertThat(chunks).hasSize(size).extracting("type", "transactionType", "unitPrice")
                .contains(tuples);
    }

    private Discount getDiscount(AmountType type, BigDecimal amount, String code) {
        Discount discount = new Discount();
        discount.setId(RandomUtils.nextLong());
        discount.setCouponCode(code);
        discount.setAmountType(type);
        discount.setAmount(amount);
        return discount;
    }

}
