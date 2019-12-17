package com.active.services.cart.service;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.quote.CartPriceEngine;
import com.active.services.cart.util.DataAccess;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartServiceTestCase {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartPriceEngine cartPriceEngine;

    @Mock
    private DataAccess dataAccess;

    @InjectMocks
    private CartService cartService;

    @Test
    public void deleteSuccess() {
        cartService.delete(1L);
    }

    @Test
    public void createCartItemsSuccess() {
        cartService.createCartItems(1L, Collections.singletonList(CartDataFactory.cartItem()));
    }

    @Test
    public void updateCartItemSuccess() {
        UUID cartIdentifier = UUID.randomUUID();
        cartService.updateCartItems(cartIdentifier, Collections.singletonList(CartDataFactory.cartItem()));
    }

    @Test
    public void findCartsByIdentifierSuccess() {
        UUID identifier = UUID.randomUUID();
        Cart cart = CartDataFactory.cart();
        when(cartRepository.getCart(identifier)).thenReturn(Optional.of(cart));
        cartService.get(identifier);
        Mockito.verify(cartRepository).getCart(identifier);
    }

    @Test(expected = CartException.class)
    public void findCartsWhenIdentifierIsNotExistThrowException() {
        UUID identifier = UUID.randomUUID();
        when(cartRepository.getCart(identifier)).thenReturn(Optional.ofNullable(null));
        cartService.get(identifier);
        Mockito.verify(cartRepository).getCart(identifier);
    }

    @Test
    public void searchUUIDsByOwnerIdSuccess() {
        UUID ownerId = UUID.randomUUID();
        List<UUID> cartIds = new ArrayList<>();
        cartIds.add(UUID.randomUUID());
        when(cartRepository.search(ownerId)).thenReturn(cartIds);
        cartService.search(ownerId);
        Mockito.verify(cartRepository).search(ownerId);
    }

    @Test(expected = CartException.class)
    public void searchUUIDsByOwnerIdWhenCartNotExist() {
        UUID ownerId = UUID.randomUUID();
        List<UUID> cartIds = new ArrayList<>();
        when(cartRepository.search(ownerId)).thenReturn(cartIds);
        cartService.search(ownerId);
        Mockito.verify(cartRepository).search(ownerId);
    }

    @Test
    public void deleteCartItemSuccess() {
        UUID cartId = UUID.randomUUID();
        UUID cartItemId = UUID.randomUUID();
        cartService.deleteCartItem(cartId, cartItemId);
        Mockito.verify(cartRepository).deleteCartItem(cartItemId);
    }

    @Test
    public void createCartSuccess() {
        Cart cart = CartDataFactory.cart();
        cartService.create(cart);
        Mockito.verify(cartRepository).createCart(cart);
    }

    @Test
    public void quoteSuccess() {
        UUID cartId = UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D");
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setId(1L);
        CartItemFee cartItemFee = CartDataFactory.cartItemFee();
        cartItem.setFees(Arrays.asList(cartItemFee));
        cart.setItems(Arrays.asList(cartItem));
        when(cartRepository.getCart(cartId)).thenReturn(Optional.ofNullable(cart));
        doNothing().when(cartPriceEngine).quote(any());
        cartService.quote(cartId);
    }
}
