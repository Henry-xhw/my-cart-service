package com.active.services.cart.service.quote;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;
import com.active.services.product.Product;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CartQuoteContextTestCase {

    @Test
    public void getProductIdsForAllFlattenCartItem() {
        Cart cart = CartDataFactory.cart();
        cart.getItems().get(0).setSubItems(Collections.singletonList(CartDataFactory.cartItem()));

        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        assertEquals(3, cartQuoteContext.getProductIds().size());
    }

    @Test
    public void setProducts() {
        Cart cart = CartDataFactory.cart();
        cart.getItems().get(0).setSubItems(Collections.singletonList(CartDataFactory.cartItem()));
        List<Product> products = cart.getFlattenCartItems().stream().map(CartItem::getProductId)
                .map(this::getProduct).collect(Collectors.toList());
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        cartQuoteContext.setProducts(products);
        assertEquals(3, cartQuoteContext.getProductsMap().size());
        Long productId = cart.getItems().get(0).getProductId();
        assertEquals(getProduct(productId), cartQuoteContext.getProductsMap().get(productId));
    }

    @Test
    public void getDiscountModel() {
        Cart cart = CartDataFactory.cart();
        cart.getItems().get(0).setSubItems(Collections.singletonList(CartDataFactory.cartItem()));
        List<Product> products = cart.getFlattenCartItems().stream().map(CartItem::getProductId)
                .map(this::getProduct).collect(Collectors.toList());
        products.get(0).setDiscountModel(DiscountModel.NON_COMBINABLE_MINIMIZE_PRICE);
        products.get(1).setDiscountModel(DiscountModel.COMBINABLE_FLAT_FIRST);

        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        cartQuoteContext.setProducts(products);
        assertEquals(DiscountModel.NON_COMBINABLE_MINIMIZE_PRICE,
                cartQuoteContext.getDiscountModel(products.get(0).getId()));

        assertEquals(DiscountModel.COMBINABLE_FLAT_FIRST,
                cartQuoteContext.getDiscountModel(products.get(1).getId()));

        assertEquals(DiscountModel.COMBINABLE_FLAT_FIRST,
                cartQuoteContext.getDiscountModel(products.get(2).getId()));
    }

    @Test
    public void getCartLevelCouponCodes() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);

        assertTrue(CollectionUtils.isEmpty(cartQuoteContext.getCartLevelCouponCodes()));

        cart = CartDataFactory.cart();
        cart.setCouponCodes(new HashSet<>(Arrays.asList("code", "coupon")));
        cartQuoteContext = new CartQuoteContext(cart);
        assertEquals(cart.getCouponCodes(), cartQuoteContext.getCartLevelCouponCodes());
    }

    @Test
    public void getUsedUniqueCouponDiscountsIds() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);


        cartQuoteContext.addAppliedDiscount(new Discount(
                "name", "disc", new BigDecimal(2), AmountType.FLAT,
                RandomUtils.nextLong(), DiscountType.MULTI, "cde", DiscountAlgorithm.MOST_EXPENSIVE));


        cartQuoteContext.addAppliedDiscount(new Discount(
                "name", "disc", new BigDecimal(2), AmountType.FLAT,
                RandomUtils.nextLong(), DiscountType.COUPON, "cde", null));

        assertEquals(2, cartQuoteContext.getAppliedDiscounts().size());
        assertTrue(CollectionUtils.isEmpty(cartQuoteContext.getUsedUniqueCouponDiscountsIds()));

        Long id = RandomUtils.nextLong();
        cartQuoteContext.addAppliedDiscount(new Discount(
                "name", "disc", new BigDecimal(2), AmountType.FLAT, id,
                DiscountType.COUPON, "cde", DiscountAlgorithm.MOST_EXPENSIVE));
        assertEquals(3, cartQuoteContext.getAppliedDiscounts().size());

        assertEquals(1, cartQuoteContext.getUsedUniqueCouponDiscountsIds().size());
        assertEquals(id, cartQuoteContext.getUsedUniqueCouponDiscountsIds().get(0));
    }

    @Test
    public void getCurrency() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        assertEquals(Currency.getInstance(cart.getCurrencyCode()), cartQuoteContext.getCurrency());
    }

    private Product getProduct(Long id) {
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
