package com.active.services.cart.application.impl;

import com.active.services.ProductType;
import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.infrastructure.repository.ProductMembership;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.mockito.Mockito.when;

/**
 * TODO: Jeff - don't judge test case here, just to verify ideas
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MembershipDiscountEngineTestCase {
    @MockBean private ProductServiceSoap productRepo;
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

        ProductMembership pm = new ProductMembership();
        pm.setProductId(nonMembership.getId());
        pm.setMembershipId(membershipDiscountHistory.getMembershipId());
        when(productRepo.findProductMemberships(Collections.singletonList(nonMembership.getId()))).thenReturn(Collections.singletonList(pm));

        Cart cart = cart();
        CartItem item = new CartItem();
        item.setPersonIdentifier(UUID.randomUUID().toString());
        item.setProductId(nonMembership.getId());
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
        cart.getItems().add(item);

        engine.apply(cart);
    }

    @Test
    public void applyMembershipDiscountToEmptyCart() {
        engine.apply(cart());
    }

    @Test
    public void applyMembershipDiscountToCartWithOnlyMembershipProduct() {
        Product membershipProd = membershipProduct();
        when(productRepo.getProduct(membershipProd.getId())).thenReturn(Optional.of(membershipProd));

        Cart cart = cart();
        CartItem item = new CartItem();
        item.setPersonIdentifier(UUID.randomUUID().toString());
        item.setProductId(membershipProd.getId());
        item.setQuantity(1);
        item.setFees(new ArrayList<>());
        item.getFees().add(CartItemFee.builder()
                .name("price")
                .unitPrice(BigDecimal.TEN)
                .units(1)
                .transactionType(FeeTransactionType.DEBIT)
                .type(CartItemFeeType.PRICE)
                .build());
        cart.getItems().add(item);
        engine.apply(cart);
    }

    private Cart cart() {
        Cart cart = new Cart();
        cart.setCurrencyCode("USD");
        cart.setItems(new ArrayList<>());
        return cart;
    }

    private Product nonMembershipProduct() {
        Product product = new Product();
        product.setId(ThreadLocalRandom.current().nextLong());
        product.setProductType(ProductType.REGISTRATION);
        return product;
    }

    private Product membershipProduct() {
        Product product = new Product();
        product.setId(ThreadLocalRandom.current().nextLong());
        product.setProductType(ProductType.MEMBERSHIP);
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
