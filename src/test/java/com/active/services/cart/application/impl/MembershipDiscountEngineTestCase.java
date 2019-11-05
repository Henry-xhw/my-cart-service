package com.active.services.cart.application.impl;

import com.active.services.ProductType;
import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.AmountType;
import com.active.services.product.Product;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.when;

/**
 * TODO: Jeff - don't judge test case here, just to verify ideas
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MembershipDiscountEngineTestCase {
    @MockBean private ProductRepository productRepo;
    @Autowired private MembershipDiscountEngine engine;

    @Test
    public void applyMembershipDiscount() {
        Product nonMembership = nonMembershipProduct();
        when(productRepo.getProduct(nonMembership.getId())).thenReturn(Optional.of(nonMembership));
        Map<Long, List<MembershipDiscountsHistory>> membershipDiscount = new HashMap<>();
        membershipDiscount.put(nonMembership.getId(), new ArrayList<>());

        MembershipDiscountsHistory membershipDiscountHistory = membershipDiscount();
        membershipDiscount.get(nonMembership.getId()).add(membershipDiscountHistory);
        when(productRepo.findMembershipDiscounts(Collections.singletonList(nonMembership.getId()))).thenReturn(membershipDiscount);

        Cart cart = new Cart();
        cart.setPriceDate(LocalDateTime.now());
        List<CartItem> items = new ArrayList<>();
        CartItem item = new CartItem();
        item.setProductId(nonMembership.getId());
        item.setQuantity(1);
        items.add(item);
        cart.setCartItems(items);
        engine.apply(cart);
    }

    private Product nonMembershipProduct() {
        Product product = new Product();
        product.setId(ThreadLocalRandom.current().nextLong());
        product.setProductType(ProductType.REGISTRATION);
        return product;
    }

    private MembershipDiscountsHistory membershipDiscount() {
        MembershipDiscountsHistory membershipDiscountHistory = new MembershipDiscountsHistory();
        membershipDiscountHistory.setName("membership discount");
        membershipDiscountHistory.setDescription("membership discount descr");
        membershipDiscountHistory.setAmount(BigDecimal.TEN);
        membershipDiscountHistory.setAmountType(AmountType.PERCENT);
        membershipDiscountHistory.setMembershipId(ThreadLocalRandom.current().nextLong());
        membershipDiscountHistory.setId(ThreadLocalRandom.current().nextLong());
        return membershipDiscountHistory;
    }
}
