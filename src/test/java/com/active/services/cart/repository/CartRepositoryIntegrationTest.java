package com.active.services.cart.repository;

import com.active.services.cart.CartServiceApp;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeeRelationship;
import com.active.services.cart.service.CartStatus;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CartServiceApp.class)
@Transactional
@Rollback
@ActiveProfiles("local")
@Ignore
public class CartRepositoryIntegrationTest {

    private static final String identifier = "34D725FD-85CC-4724-BA6E-1B3CC41CDE31";
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemFeeRepository cartItemFeeRepository;

    @Test
    public void cartCRUD() {
        Cart cart = CartDataFactory.cart();
        cartRepository.createCart(cart);
        cartRepository.updateCart(cart);
        cartRepository.deleteCart(cart.getId());
        assertNotNull(cart.getId());
    }

    @Test
    public void cartItemCRUD() {
        Cart cart = CartDataFactory.cart();
        cartRepository.createCart(cart);
        CartItem cartItem = CartDataFactory.cartItem();
        cartRepository.createCartItems(cart.getId(), Collections.singletonList(cartItem));
        List<UUID> uuidList = cartRepository.search(cart.getOwnerId());
        Assert.assertThat(uuidList.size(), Matchers.equalTo(1));
        assertNotNull(cartItem.getId());
        cartRepository.getCart(cart.getIdentifier()).ifPresent(cart1 -> {
            Assert.assertThat(cart1.getItems().size(), Matchers.equalTo(1));
            Assert.assertThat(cart1.getItems().get(0).getProductName(), Matchers.equalTo(cartItem.getProductName()));
            cart1.getItems().get(0).setProductName("test update cartItem");
            cart1.getItems().get(0).setQuantity(300);
            cartRepository.updateCartItems(cart1.getItems());
            cartRepository.getCart(cart.getIdentifier()).ifPresent(cart2 -> {
                Assert.assertThat(cart2.getItems().size(), Matchers.equalTo(1));
                Assert.assertThat(cart2.getItems().get(0).getQuantity(), Matchers.equalTo(300));
                Assert.assertThat(cart2.getItems().get(0).getProductName(), Matchers.equalTo("test update cartItem"));
                cartRepository.deleteCartItem(cartItem.getId());
                cartRepository.getCart(cart.getIdentifier()).ifPresent(cart3 -> {
                    Assert.assertThat(cart3.getItems().size(), Matchers.equalTo(0));
                });
            });
        });
    }

    @Test
    public void cartItemFeeCRUD() {
        Cart cart = CartDataFactory.cart();
        cartRepository.createCart(cart);
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setId(1L);
        CartItemFee cartItemFee = CartDataFactory.cartItemFee();
        cartItem.setFees(Arrays.asList(cartItemFee));
        cartRepository.createCartItem(cart.getId(), cartItem);
        cartItemFeeRepository.createCartItemFee(cartItemFee);
        cartItemFeeRepository.createCartItemCartItemFee(
            CartItemFeeRelationship.buildCartItemCartItemFee(cartItem.getId(), cartItemFee.getId()));
        cartItemFeeRepository.deleteLastQuoteResult(cartItem.getId());
        assertNotNull(cartItem.getId());
    }

    @Test
    public void cartStatusTest(){
        Cart cart = CartDataFactory.cart();
        cartRepository.createCart(cart);
        Assert.assertEquals(CartStatus.CREATED, cartRepository.getCart(cart.getIdentifier()).get().getCartStatus());

        cartRepository.finalizeCart(cart.getIdentifier(), "actorId");
        Assert.assertEquals(false, cartRepository.getCart(cart.getIdentifier()).isPresent());
    }

    @Test
    public void cartVersionTest(){
        Cart cart = CartDataFactory.cart();
        cartRepository.createCart(cart);
        Assert.assertEquals(0, cartRepository.getCart(cart.getIdentifier()).get().getVersion(), 0);

        cartRepository.incrementVersion(cart.getIdentifier(), "actorId");
        Assert.assertEquals(1, cartRepository.getCart(cart.getIdentifier()).get().getVersion());

        cartRepository.finalizeCart(cart.getIdentifier(), "actorId");
        Assert.assertEquals(0, cartRepository.incrementVersion(cart.getIdentifier(), "actorId"));
    }

    @Test
    public void cartPriceVersionTest(){
        Cart cart = CartDataFactory.cart();
        cartRepository.createCart(cart);
        cartRepository.incrementVersion(cart.getIdentifier(), "actorId");
        cartRepository.incrementVersion(cart.getIdentifier(), "actorId");
        Assert.assertEquals(1, cartRepository.incrementPriceVersion(cart.getIdentifier(), "actorId"));
        Assert.assertEquals(2, cartRepository.getCart(cart.getIdentifier()).get().getPriceVersion());
    }

    @Test
    public void cartLockTest(){
        Cart cart = CartDataFactory.cart();
        cartRepository.createCart(cart);
        cartRepository.acquireLock(cart.getIdentifier(), "actorId");
        Assert.assertEquals(true, cartRepository.getCart(cart.getIdentifier()).get().isLock());
        cartRepository.releaseLock(cart.getIdentifier(), "actorId");
        Assert.assertEquals(false, cartRepository.getCart(cart.getIdentifier()).get().isLock());
    }
}
