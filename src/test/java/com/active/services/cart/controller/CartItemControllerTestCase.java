package com.active.services.cart.controller;

import com.active.services.cart.common.OperationResultCode;
import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.controller.v1.CartItemController;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.rsp.DeleteCartItemRsp;
import com.active.services.cart.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public class CartItemControllerTestCase {
    @Mock
    private CartService cartService;
    @InjectMocks
    private CartItemController itemController;
    private Cart cart;
    private UUID cartId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private UUID cartItemId1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    private UUID cartItemId2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");

    @Before
    public void setUp() {
        cart = new Cart();
        cart.setIdentifier(cartId);

        CartItem cartItem1 = new CartItem();
        cartItem1.setIdentifier(cartItemId1);
        cart.getItems().add(cartItem1);
        CartItem cartItem2 = new CartItem();
        cartItem2.setIdentifier(cartItemId2);
        cart.getItems().add(cartItem2);
    }


    @Test
    public void delete_Common() throws Exception {
        Mockito.when(cartService.get(cartId)).thenReturn(cart);
        DeleteCartItemRsp rsp = itemController.delete(cartId, cartItemId1);
        assert (rsp.getCartId().equals(cartId));
    }

    @Test
    public void delete_cartNotExist() throws Exception {
        Mockito.when(cartService.get(cartId)).thenReturn(null);
        try {
            itemController.delete(cartId, cartItemId1);
        } catch (CartException exception) {
            assert (exception.getErrorCode() == OperationResultCode.CART_NOT_EXIST.getCode());
        }
    }

    @Test
    public void delete_cartItemNotExist() throws Exception {
        Mockito.when(cartService.get(cartId)).thenReturn(cart);
        try {
            itemController.delete(cartId, UUID.fromString("550e8400-e29b-41d4-a716-446655440003"));
        } catch (CartException exception) {
            assert (exception.getErrorCode() == OperationResultCode.CART_ITEM_NOT_EXIST.getCode());
        }
    }

    @Test
    public void delete_cartItemNotExist_emptyCart() throws Exception {
        cart.getItems().clear();
        Mockito.when(cartService.get(cartId)).thenReturn(cart);
        try {
            itemController.delete(cartId, cartItemId1);
        } catch (CartException exception) {
            assert (exception.getErrorCode() == OperationResultCode.CART_ITEM_NOT_EXIST.getCode());
        }
    }

}
