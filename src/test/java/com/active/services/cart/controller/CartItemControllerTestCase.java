package com.active.services.cart.controller;

import com.active.services.cart.common.CartException;
import com.active.services.cart.controller.v1.CartItemController;
import com.active.services.cart.controller.v1.mapper.CartMapper;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.CartItemDto;
import com.active.services.cart.model.v1.UpdateCartItemDto;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.model.v1.req.UpdateCartItemReq;
import com.active.services.cart.service.CartService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.active.services.cart.controller.Constants.V1_MEDIA;
import static com.active.services.cart.restdocs.RestDocument.autoPathParameterDoc;
import static com.active.services.cart.restdocs.RestDocument.autoRequestFieldsDoc;
import static com.active.services.cart.restdocs.RestDocument.newErrorDocument;
import static com.active.services.cart.restdocs.RestDocument.newSuccessDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CartItemController.class, secure = false)
public class CartItemControllerTestCase extends BaseControllerTestCase {

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
    public void createCartItemSuccess() throws Exception {
        when(cartService.getCartByUuid(any(UUID.class))).thenReturn(CartDataFactory.cart());
        CreateCartItemReq req = new CreateCartItemReq();
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(CartDataFactory.cartItem())));
        mockMvc.perform(post("/api/carts/{cart-id}/items", cartId)
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk())
          .andDo(newSuccessDocument("Cart-Item", "Create-Cart-Item",
            pathParameters(autoPathParameterDoc("cart-id", CartDto.class, "identifier")),
            autoRequestFieldsDoc(req)));
    }

    @Test
    public void createCartItemWhenCartNotExistThrowException() throws Exception {
        when(cartService.getCartByUuid(any(UUID.class))).thenThrow(new CartException(ErrorCode.CART_NOT_FOUND, "cart " +
                "not found"));
        CreateCartItemReq req = new CreateCartItemReq();
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(CartDataFactory.cartItem())));
        mockMvc.perform(post("/api/carts/{cart-id}/items", cartId)
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk())
          .andDo(newErrorDocument("Cart-Item", "Create-Cart-Item", "Cart-Item-Not-Exist"));
    }

    @Test
    public void deleteCartItemSuccess() throws Exception {
        when(cartService.getCartByUuid(any(UUID.class))).thenReturn(cart);
        mockMvc.perform(delete("/api/carts/{cart-id}/items/{cart-item-id}",
                cartId, cartItemId1)
                .headers(actorIdHeader())
                .contentType(V1_MEDIA))
                .andExpect(status().isOk())
                .andDo(newSuccessDocument("Cart-Item", "Delete-Cart-Item",
                        pathParameters(autoPathParameterDoc("cart-id", CartDto.class, "identifier"),
                                autoPathParameterDoc("cart-item-id", CartItemDto.class, "identifier"))));
    }

    @Test
    public void deleteCartItemWhenCartNotExistThrowException() throws Exception {
        when(cartService.getCartByUuid(any(UUID.class))).thenThrow(new CartException(ErrorCode.CART_NOT_FOUND, "cart " +
                "no found"));
        mockMvc.perform(delete("/api/carts/{cart-id}/items/{cart-item-id}",
                cartId, cartItemId1)
                .headers(actorIdHeader())
                .contentType(V1_MEDIA))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Delete-Cart-Item", "Cart-Not-Exist"));
    }

    @Test
    public void deleteCartItemWhenCartItemNotExistThrowException() throws Exception {
        when(cartService.getCartByUuid(any(UUID.class))).thenReturn(cart);
        mockMvc.perform(delete("/api/carts/{cart-id}/items/{cart-item-id}",
                cartId, cartItemId2)
                .headers(actorIdHeader())
                .contentType(V1_MEDIA))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Delete-Cart-Item", "Cart-Item-Not-Exist"));
    }

    @Test
    public void updateCartItemSuccess() throws Exception {
        UpdateCartItemReq req = new UpdateCartItemReq();
        UUID identifier = UUID.randomUUID();
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        UpdateCartItemDto updateCartItemDto = CartDataFactory.updateCartItemDto(cartItem);
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);
        req.setItems(Collections.singletonList(updateCartItemDto));
        when(cartService.getCartByUuid(identifier)).thenReturn(cart);
        when(cartService.updateCartItems(identifier, items)).thenReturn(items);
        mockMvc.perform(put("/api/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newSuccessDocument("Cart-Item", "Update-Cart-Item",
                        pathParameters(autoPathParameterDoc("cart-id", CartDto.class, "identifier")),
                        autoRequestFieldsDoc(req)));
    }

    @Test
    public void updateCartItemWhenCartItemNotExistThrowException() throws Exception {
        UpdateCartItemReq req = new UpdateCartItemReq();
        UUID identifier = UUID.randomUUID();
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        UpdateCartItemDto updateCartItemDto = CartDataFactory.updateCartItemDto(cartItem);
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);
        req.setItems(Collections.singletonList(updateCartItemDto));
        when(cartService.getCartByUuid(identifier)).thenThrow(new CartException(ErrorCode.CART_NOT_FOUND, "cart not " +
                "found"));
        when(cartService.updateCartItems(identifier, items)).thenReturn(items);
        mockMvc.perform(put("/api/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Update-Cart-Item", "Cart-Item-Not-Exist"));
    }

    @Test
    public void updateCartItemWhenIdentifierIsNullThrowException() throws Exception {
        UpdateCartItemReq req = new UpdateCartItemReq();
        UUID identifier = UUID.randomUUID();
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        UpdateCartItemDto updateCartItemDto = CartDataFactory.updateCartItemDto(cartItem);
        updateCartItemDto.setIdentifier(null);
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);
        req.setItems(Collections.singletonList(updateCartItemDto));
        when(cartService.getCartByUuid(identifier)).thenReturn(cart);
        when(cartService.updateCartItems(identifier, items)).thenReturn(items);
        mockMvc.perform(put("/api/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Update-Cart-Item", "Identifier-Is-Null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMessage").hasJsonPath());
    }

    @Test
    public void updateCartItemWhenCartItemIsNotBelongCartThrowException() throws Exception {
        UpdateCartItemReq req = new UpdateCartItemReq();
        UUID identifier = UUID.randomUUID();
        Cart cart = CartDataFactory.cart();
        CartItem cartItem1 = CartDataFactory.cartItem();
        UpdateCartItemDto updateCartItemDto = CartDataFactory.updateCartItemDto(cartItem1);
        CartItem cartItem2 = new CartItem();
        cartItem2.setIdentifier(UUID.randomUUID());
        cartItem2.setIdentifier(UUID.randomUUID());
        cartItem2.setProductId(2L);
        cartItem2.setProductName("product name");
        cartItem2.setProductDescription("product description");
        cartItem2.setBookingRange(null);
        cartItem2.setTrimmedBookingRange(null);
        cartItem2.setQuantity(2);
        cartItem2.setUnitPrice(BigDecimal.ONE);
        cartItem2.setGroupingIdentifier("grouping identifier");
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem2);
        cart.setItems(items);
        req.setItems(Collections.singletonList(updateCartItemDto));
        when(cartService.getCartByUuid(identifier)).thenReturn(cart);
        when(cartService.updateCartItems(identifier, items)).thenReturn(items);
        mockMvc.perform(put("/api/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Update-Cart-Item", "Cart-Item-Is-Not-Belong-Cart"));
    }
}
