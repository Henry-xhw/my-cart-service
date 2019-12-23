package com.active.services.cart.service;

import com.active.services.cart.client.rest.OrderService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.cart.model.v1.req.CheckoutReq;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.util.AuditorAwareUtil;
import com.active.services.order.management.api.v3.types.OrderResponseDTO;
import com.active.services.order.management.api.v3.types.PlaceOrderRsp;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.active.services.cart.domain.CartDataFactory.cartItem;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartServiceTestCase {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CartService cartService;

    @Test
    public void deleteSuccess() {
        cartService.delete(1L);
    }

    @Test
    public void createCartItemsSuccess() {
        cartService.createCartItems(1L, UUID.randomUUID(), Collections.singletonList(cartItem()));
    }

    @Test
    public void updateCartItemSuccess() {
        cartService.updateCartItems(UUID.randomUUID(), Collections.singletonList(cartItem()));
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
        UUID cartItemId = UUID.randomUUID();
        cartService.deleteCartItem(CartDataFactory.cart(), cartItemId);
        Mockito.verify(cartRepository).batchDeleteCartItems(anyList());
    }

    @Test
    public void createCartSuccess() {
        Cart cart = CartDataFactory.cart();
        cartService.create(cart);
        Mockito.verify(cartRepository).createCart(cart);
    }



    @Test
    public void checkoutNullCart() {
        UUID cartId = UUID.randomUUID();
        when(cartRepository.getCart(cartId)).thenReturn(Optional.ofNullable(null));
        try {
            cartService.checkout(cartId, new CheckoutReq());
            fail("should fail when there is no cart");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void checkoutWhenLockCartFailed() {
        Cart cart = CartDataFactory.cart();
        UUID cartId = UUID.randomUUID();
        when(cartRepository.getCart(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.acquireLock(any(), anyString())).thenReturn(0);
        try {
            cartService.checkout(cartId, new CheckoutReq());
            fail("should fail when there is no cart");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_LOCKED, e.getErrorCode());
        }
    }

    @Test
    public void checkoutWhenCartWithoutCartItem() {
        Cart cart = CartDataFactory.cart();
        UUID cartId = UUID.randomUUID();
        cart.setItems(new ArrayList<>());
        when(cartRepository.getCart(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.acquireLock(any(), anyString())).thenReturn(1);
        try {
            cartService.checkout(cartId, new CheckoutReq());
            fail("should fail when there is no cart");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_ITEM_NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void checkoutWhen() {
        Cart cart = CartDataFactory.cart();
        List<CartItem> items = new ArrayList<>();
        items.add(CartDataFactory.cartItem());
        cart.setItems(items);
        UUID cartId = UUID.randomUUID();
        when(cartRepository.getCart(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.acquireLock(any(), anyString())).thenReturn(1);
        Long orderId = RandomUtils.nextLong();
        when(orderService.placeOrder(any())).thenReturn(buildRsp(orderId));
        try {
            List<CheckoutResult> results = cartService.checkout(cartId, new CheckoutReq());
            assertTrue(CollectionUtils.isNotEmpty(results));
            assertEquals(orderId, results.get(0).getOrderId());
        } catch (CartException e) {
            fail("no exception");
        }
    }

    private PlaceOrderRsp buildRsp(Long orderId) {
        PlaceOrderRsp rsp = new PlaceOrderRsp();
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setOrderId(orderId);
        List<OrderResponseDTO> list = new ArrayList<>();
        list.add(dto);
        rsp.setOrderResponses(list);
        return rsp;
    }
}
