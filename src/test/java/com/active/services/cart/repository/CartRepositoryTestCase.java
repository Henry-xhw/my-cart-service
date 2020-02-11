package com.active.services.cart.repository;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.repository.mapper.CartMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartRepositoryTestCase {
    private static final String identifier = "34D725FD-85CC-4724-BA6E-1B3CC41CDE31";

    private CartRepository cartRepository;

    @Mock
    private CartMapper cartMapper;

    @Before
    public void setUp() {
        cartRepository = new CartRepository(cartMapper);
    }

    @Test
    public void cartCRUD() {
        Cart cart = CartDataFactory.cart();
        cartRepository.createCart(cart);
        verify(cartMapper, times(1)).createCart(any());

        cartRepository.deleteCart(1L);
        verify(cartMapper, times(1)).deleteCart(any());

        when(cartMapper.getCart(any())).thenReturn(Optional.of(cart));
        Optional<Cart> cartGot = cartRepository.getCart(UUID.fromString(identifier));
        Assert.assertTrue(cartGot.isPresent());

        List<UUID> mockList = new ArrayList();
        mockList.add(UUID.randomUUID());
        when(cartMapper.search(any())).thenReturn(mockList);
        List<UUID> searchList = cartRepository.search(UUID.randomUUID());
        Assert.assertFalse(searchList.isEmpty());
    }

    @Test
    public void cartStatusOperation() {
        when(cartMapper.finalizeCart(any(), any())).thenReturn(1);
        int result = cartRepository.finalizeCart(UUID.randomUUID(), "system");
        Assert.assertEquals(1, result);
    }

    @Test
    public void cartVersionOperation() {
        when(cartMapper.incrementVersion(any(), any())).thenReturn(1);
        int result = cartRepository.incrementVersion(UUID.randomUUID(), "system");
        Assert.assertEquals(1, result);

        when(cartMapper.incrementPriceVersion(any(), any())).thenReturn(1);
        int ret = cartRepository.incrementPriceVersion(UUID.randomUUID(), "system");
        Assert.assertEquals(1, ret);

    }

    @Test
    public void cartLockOperation() {
        when(cartMapper.acquireLock(any(), any())).thenReturn(1);
        int result = cartRepository.acquireLock(UUID.randomUUID(), "system");
        Assert.assertEquals(1, result);

        when(cartMapper.releaseLock(any(), any())).thenReturn(1);
        int rest = cartRepository.releaseLock(UUID.randomUUID(), "system");
        Assert.assertEquals(1, rest);

    }

}
