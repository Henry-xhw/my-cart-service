package com.active.services.cart.service;

import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.repository.CartRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@RunWith(SpringRunner.class)
public class CartServiceTestCase {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    public void createCartItemsSuccess() {
        cartService.createCartItems(1L, Collections.singletonList(CartDataFactory.cartItem()));
    }
}
