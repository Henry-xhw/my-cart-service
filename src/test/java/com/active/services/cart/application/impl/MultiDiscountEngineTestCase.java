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
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.MultiDiscount;

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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.when;

/**
 * TODO: Jeff - don't judge test case here, just to verify ideas
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MultiDiscountEngineTestCase {
    @MockBean private ProductRepository productRepo;
    @Autowired private MultiDiscountEngine engine;

    @Test
    public void applyToEmptyCart() {
        engine.apply(cart());
    }

    @Test
    public void applyMDWithoutThresholdSetting() {
        Cart cart = cart();
        Product product = fakeProduct();
        cart.getCartItems().add(cartItem(product.getId()));

        when(productRepo.findEffectiveMultiDiscountsByProductId(product.getId(), cart.getPriceDate())).thenReturn(Collections.singletonList(mdWithoutThresholdSetting()));

        engine.apply(cart);
    }

    private MultiDiscount mdWithoutThresholdSetting() {
        MultiDiscount md = new MultiDiscount();
        md.setName("md");
        md.setDescription("multi discount");
        md.setThreshold(3);
        Set<DiscountTier> tiers = new HashSet<>();
        DiscountTier t1 = discountTier(1, BigDecimal.TEN);
        DiscountTier t2 = discountTier(2, BigDecimal.valueOf(20));
        DiscountTier t3 = discountTier(3, BigDecimal.valueOf(30));
        tiers.add(t1);
        tiers.add(t2);
        tiers.add(t3);

        md.setTiers(tiers);
        return md;
    }

    private DiscountTier discountTier(int level, BigDecimal amount) {
        DiscountTier t = new DiscountTier();
        t.setTierLevel(level);
        t.setAmountType(AmountType.PERCENT);
        t.setAmount(amount);
        return t;
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
