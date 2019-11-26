package com.active.services.cart.repository;

import com.active.services.cart.CartServiceApp;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.TestCase.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CartServiceApp.class)
@Transactional
@Rollback
public class CartRepositoryIntegrationTest {

    @Autowired
    private CartRepository cartRepository;

    @Test
    public void cartCRUD() {
        Cart cart = CartDataFactory.cart();
        cartRepository.createCart(cart);
        cartRepository.deleteCart(cart.getId());
        assertNotNull(cart.getId());
    }
}
