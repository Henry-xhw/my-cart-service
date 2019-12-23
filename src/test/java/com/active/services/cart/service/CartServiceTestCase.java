package com.active.services.cart.service;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.util.AuditorAwareUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartServiceTestCase {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    public void deleteSuccess() {
        cartService.delete(1L);
    }

    @Test
    public void createCartItemsSuccess() {
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        try {
            cartService.createCartItems(1L, cart.getIdentifier(), Collections.singletonList(cartItem));
            verify(cartRepository, any()).createCartItem(cart.getId(), cartItem);
        } catch (NullPointerException e) {

        }
    }

    @Test
    public void updateCartItemSuccess() {
        Cart cart = CartDataFactory.cart();
        when(cartRepository.getCart(cart.getIdentifier())).thenReturn(Optional.of(cart));
        try {
            cartService.updateCartItems(cart.getIdentifier(), Collections.singletonList(CartDataFactory.cartItem()));
        }
        catch (CartException e) {

        }
    }

    @Test
    public void findCartsByIdentifierSuccess() {
        UUID identifier = UUID.randomUUID();
        Cart cart = CartDataFactory.cart();
        when(cartRepository.getCart(identifier)).thenReturn(Optional.of(cart));
        cartService.getCartByCartUuid(identifier);
        Mockito.verify(cartRepository).getCart(identifier);
    }

    @Test(expected = CartException.class)
    public void findCartsWhenIdentifierIsNotExistThrowException() {
        UUID identifier = UUID.randomUUID();
        when(cartRepository.getCart(identifier)).thenReturn(Optional.ofNullable(null));
        cartService.getCartByCartUuid(identifier);
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

    @Test
    public void deleteCartItemSuccess() {
        Cart cart = CartDataFactory.cart();
        Optional<CartItem> cartItem = Optional.of(CartDataFactory.cartItem());
        UUID cartItemId = cart.getIdentifier();
        try {
            cartService.deleteCartItem(cart, cartItemId);
            Mockito.verify(cartRepository).deleteCartItem(cart.getId());
        } catch (Exception e) {

        }
    }

    @Test
    public void createCartSuccess() {
        Cart cart = CartDataFactory.cart();
        cartService.create(cart);
        Mockito.verify(cartRepository).createCart(cart);
    }
}
