package com.active.services.cart.repository;

import com.active.services.cart.CartServiceApp;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = CartServiceApp.class)
@Transactional
@Rollback
public class CartRepositoryIntegrationTest {

    @Test
    public void cartCRUD() {

    }
}
