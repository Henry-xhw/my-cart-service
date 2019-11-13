package com.active.services.cart.application.impl;

import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.cart.CartItemFee;
import com.active.services.cart.infrastructure.repository.ProductRepository;
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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
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
    @MockBean private ProductRepository productRepo;
    @Autowired private CouponDiscountEngine engine;

    @Test
    public void applyToEmptyCartNoError() {
        engine.apply(cart(), null);
    }

    @Test
    public void apply() {
        Cart cart = cart();
        Product product = fakeProduct();
        cart.getCartItems().add(cartItem(product.getId()));
        Discount discount = discount();

        when(productRepo.getProduct(product.getId())).thenReturn(Optional.of(product));
        when(productRepo.findDiscountByProductIdAndCode(product.getId(), discount.getCouponCode())).thenReturn(Collections.singletonList(discount));
        when(productRepo.isDiscountLimitReached(discount, cart.getCartItems().get(0).getQuantity(),
                cart.getCartItems().get(0).getId())).thenReturn(false);

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
        item.setCartItemFees(new ArrayList<>());
        item.getCartItemFees().add(CartItemFee.builder()
                .name("price")
                .unitPrice(BigDecimal.TEN)
                .units(1)
                .transactionType(FeeTransactionType.DEBIT)
                .feeType(CartItemFeeType.PRICE)
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
        cart.setCurrency(Currency.getInstance("USD"));
        cart.setPriceDate(LocalDateTime.now());
        cart.setCartItems(new ArrayList<>());
        return cart;
    }
}
