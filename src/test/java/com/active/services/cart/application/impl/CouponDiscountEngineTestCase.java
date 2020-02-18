package com.active.services.cart.application.impl;

import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.Product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.when;

/**
 * TODO: Jeff - don't judge test case here, just to verify ideas
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CouponDiscountEngineTestCase {
    @MockBean private ProductServiceSoap productRepo;
    @Autowired private CouponDiscountEngine engine;

    @Test
    public void applyToEmptyCartNoError() {
        engine.apply(cart(), null);
    }

    @Test
    public void apply() {
        Cart cart = cart();
        Product product = fakeProduct();
        cart.getItems().add(cartItem(product.getId()));
        Discount discount = discount();

        when(productRepo.getProduct(product.getId())).thenReturn(Optional.of(product));
        when(productRepo.findDiscountByProductIdAndCode(product.getId(), Arrays.asList(discount.getCouponCode()))).thenReturn(Collections.singletonList(discount));
        when(productRepo.isDiscountLimitReached(discount, cart.getFlattenCartItems().get(0).getQuantity(),
                cart.getFlattenCartItems().get(0).getId())).thenReturn(false);

        engine.apply(cart, discount.getCouponCode());
    }

    private Product fakeProduct() {
        Product product = Mockito.mock(Product.class);
        when(product.getId()).thenReturn(ThreadLocalRandom.current().nextLong());
        return product;
    }

    private CartItem cartItem(Long productId) {
        CartItem item = new CartItem();
        item.setId(ThreadLocalRandom.current().nextLong());
        item.setPersonIdentifier(UUID.randomUUID().toString());
        item.setProductId(productId);
        item.setQuantity(1);
        item.setFees(new ArrayList<>());
        item.setNetPrice(BigDecimal.TEN);
        item.getFees().add(CartItemFee.builder()
                .name("price")
                .unitPrice(BigDecimal.TEN)
                .units(1)
                .transactionType(FeeTransactionType.DEBIT)
                .type(CartItemFeeType.PRICE)
                .build());
        return item;
    }

    private Discount discount() {
        Discount discount = new Discount();
        discount.setAmountType(AmountType.PERCENT);
        discount.setAmount(BigDecimal.ONE);
        discount.setCouponCode(UUID.randomUUID().toString());
        return discount;
    }

    private Cart cart() {
        Cart cart = new Cart();
        cart.setId(ThreadLocalRandom.current().nextLong());
        cart.setCurrencyCode("USD");
        cart.setItems(new ArrayList<>());
        return cart;
    }
}
