package com.active.services.cart.repository;

import com.active.services.cart.common.Event;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.repository.mapper.CartMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
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
    public void cartItemCRUD() {
        List<CartItem> items = CartDataFactory.cart().getItems();
        cartRepository.createCartItems(1L, items);
        verify(cartMapper, times(items.size())).createCartItem(any(), any());

        CartItem item = CartDataFactory.cartItem();
        item.setId(1L);
        CartItemFee cartItemFee = CartDataFactory.cartItemFee();
        item.setFees(Collections.singletonList(cartItemFee));
        long cartItemId = cartRepository.createCartItem(1L, item);
        Assert.assertEquals(1L, cartItemId);
        verify(cartMapper, times(items.size()+1)).createCartItem(any(), any());

        List<CartItem> updateItems = CartDataFactory.cart().getItems();
        cartRepository.updateCartItems(updateItems);
        verify(cartMapper, times(updateItems.size())).updateCartItem(any());

        cartRepository.deleteCartItem(1L);
        verify(cartMapper, times(1)).deleteCartItem(any());

        List<UUID> deleteList = new ArrayList();
        deleteList.add(UUID.randomUUID());
        cartRepository.batchDeleteCartItems(deleteList);
        verify(cartMapper, times(1)).batchDeleteCartItems(any());

        when(cartMapper.getCartItemIdByCartItemUuid(any())).thenReturn(Optional.of(1L));
        Optional<Long> itemId = cartRepository.getCartItemIdByCartItemUuid(UUID.randomUUID());
        Assert.assertTrue(itemId.isPresent());
        Assert.assertEquals(1,itemId.get().longValue());
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

    @Test
    public void updateCartReservationIdSuccess() {

    }

    @Test
    public void createEventsSuccess() {
        List<Event> events = new ArrayList<>();
        events.add(new Event());
        cartRepository.createEvents(events);
        verify(cartMapper, times(1)).createEvents(any());
    }

}
