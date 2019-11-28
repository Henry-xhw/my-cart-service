package com.active.services.cart.controller;


import com.active.services.cart.common.OperationResultCode;
import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.controller.v1.CartItemController;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.CartItemDto;
import com.active.services.cart.service.CartService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

import static com.active.services.cart.controller.v1.Constants.V1_MEDIA;
import static com.active.services.cart.restdocs.RestDocument.autoPathParameterDoc;
import static com.active.services.cart.restdocs.RestDocument.newSuccessDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static com.active.services.cart.restdocs.RestDocument.newErrorDocument;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CartItemController.class, secure = false)
public class CartItemControllerTestCase extends BaseControllerTestCase {

    private static final String CONTENT_TYPE = "application/vnd.active.cart-service.v1+json";

    @MockBean
    private CartService cartService;
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
    }

    @Test
    public void deleteCartItemSuccess() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(cart);
        mockMvc.perform(delete("/carts/{cart-id}/items/{cart-item-id}",
                cartId, cartItemId1)
                .headers(actorIdHeader())
                .contentType(V1_MEDIA))
                .andExpect(status().isOk())
                .andDo(newSuccessDocument("Cart", "Delete-CartItem",
                        pathParameters(autoPathParameterDoc("cart-id", CartDto.class, "identifier"),
                                autoPathParameterDoc("cart-item-id", CartItemDto.class, "identifier"))));
    }

    @Test
    public void deleteCartItemFailWhenCartNotExist() throws Exception {
        when(cartService.get(any(UUID.class))).thenThrow(new CartException(OperationResultCode.CART_NOT_EXIST.getCode(),
                OperationResultCode.CART_NOT_EXIST.getDescription() + " cart id: " + cartId));
        mockMvc.perform(delete("/carts/{cart-id}/items/{cart-item-id}",
                cartId, cartItemId1)
                .headers(actorIdHeader())
                .contentType(V1_MEDIA))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart", "Delete-CartItem", "Cart-Not-Exist"));
    }

    @Test
    public void deleteCartItemFailWhenCartItemNotExist() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(cart);
        mockMvc.perform(delete("/carts/{cart-id}/items/{cart-item-id}",
                cartId, cartItemId2)
                .headers(actorIdHeader())
                .contentType(V1_MEDIA))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart", "Delete-CartItem", "CartItem-Not-Exist"));
    }

}
