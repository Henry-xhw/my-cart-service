package com.active.services.cart.controller;

import com.active.services.cart.common.OperationResultCode;
import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.controller.v1.CartItemController;
import com.active.services.cart.controller.v1.CartMapper;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.CartItemDto;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.model.v1.rsp.CreateCartItemRsp;
import com.active.services.cart.model.v1.rsp.UpdateCartItemRsp;
import com.active.services.cart.service.CartService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.active.services.cart.controller.v1.Constants.V1_MEDIA;
import static com.active.services.cart.restdocs.RestDocument.autoPathParameterDoc;
import static com.active.services.cart.restdocs.RestDocument.autoRequestFieldsDoc;
import static com.active.services.cart.restdocs.RestDocument.autoResponseFieldsDoc;
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
        when(cartService.get(any(UUID.class))).thenReturn(CartDataFactory.cart());
        CreateCartItemReq req = new CreateCartItemReq();
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(CartDataFactory.cartItem())));
        CreateCartItemRsp rsp = new CreateCartItemRsp();
        rsp.setCartId(UUID.randomUUID());
        mockMvc.perform(post("/carts/{cart-id}/items", cartId)
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk())
          .andDo(newSuccessDocument("Cart-Item", "Create-Cart-Item",
            pathParameters(autoPathParameterDoc("cart-id", CartDto.class, "identifier")),
            autoRequestFieldsDoc(req),
            autoResponseFieldsDoc(rsp)));
    }

    @Test
    public void createCartItemWhenCartNotExistThrowException() throws Exception {
        when(cartService.get(any(UUID.class))).thenThrow(new CartException(OperationResultCode.CART_NOT_EXIST.getCode(),
          OperationResultCode.CART_NOT_EXIST.getDescription() + " cart id: " + cartId));
        CreateCartItemReq req = new CreateCartItemReq();
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(CartDataFactory.cartItem())));
        mockMvc.perform(post("/carts/{cart-id}/items", cartId)
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk())
          .andDo(newErrorDocument("Cart-Item", "Create-Cart-Item", "Cart-Item-Not-Exist"));
    }

    @Test
    public void createCartItemWhenInvalidBookingRangeThrowException() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(CartDataFactory.cart());
        CreateCartItemReq req = new CreateCartItemReq();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.getBookingRange().setLower(cartItem.getBookingRange().getUpper().plusMillis(1000L));
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        mockMvc.perform(post("/carts/{cart-id}/items", cartId)
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk())
          .andDo(newErrorDocument("Cart-Item", "Create-Cart-Item", "Invalid-Booking-Range"));
    }

    @Test
    public void createCartItemWhenInvalidTrimmedBookingRangeThrowException() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(CartDataFactory.cart());
        CreateCartItemReq req = new CreateCartItemReq();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.getTrimmedBookingRange().setLower(cartItem.getTrimmedBookingRange().getUpper().plusMillis(1000L));
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        mockMvc.perform(post("/carts/{cart-id}/items", cartId)
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk())
          .andDo(newErrorDocument("Cart-Item", "Create-Cart-Item", "Invalid-Trimmed-Booking-Range"));
    }

    @Test
    public void deleteCartItemSuccess() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(cart);
        mockMvc.perform(delete("/carts/{cart-id}/items/{cart-item-id}",
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
        when(cartService.get(any(UUID.class))).thenThrow(new CartException(OperationResultCode.CART_NOT_EXIST.getCode(),
                OperationResultCode.CART_NOT_EXIST.getDescription() + " cart id: " + cartId));
        mockMvc.perform(delete("/carts/{cart-id}/items/{cart-item-id}",
                cartId, cartItemId1)
                .headers(actorIdHeader())
                .contentType(V1_MEDIA))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Delete-Cart-Item", "Cart-Not-Exist"));
    }

    @Test
    public void deleteCartItemWhenCartItemNotExistThrowException() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(cart);
        mockMvc.perform(delete("/carts/{cart-id}/items/{cart-item-id}",
                cartId, cartItemId2)
                .headers(actorIdHeader())
                .contentType(V1_MEDIA))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Delete-Cart-Item", "Cart-Item-Not-Exist"));
    }

    @Test
    public void updateCartItemSuccess() throws Exception {
        CreateCartItemReq req = new CreateCartItemReq();
        UpdateCartItemRsp rsp = new UpdateCartItemRsp();
        UUID identifier = UUID.randomUUID();
        rsp.setCartId(identifier);
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        when(cartService.get(identifier)).thenReturn(cart);
        when(cartService.updateCartItems(items)).thenReturn(items);
        mockMvc.perform(put("/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newSuccessDocument("Cart-Item", "Update-Cart-Item",
                        pathParameters(autoPathParameterDoc("cart-id", CartDto.class, "identifier")),
                        autoRequestFieldsDoc(req),
                        autoResponseFieldsDoc(rsp)));
    }

    @Test
    public void updateCartItemWhenCartItemNotExistThrowException() throws Exception {
        CreateCartItemReq req = new CreateCartItemReq();
        UpdateCartItemRsp rsp = new UpdateCartItemRsp();
        UUID identifier = UUID.randomUUID();
        rsp.setCartId(identifier);
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        when(cartService.get(identifier)).thenThrow(new CartException(OperationResultCode.CART_NOT_EXIST.getCode(),
                OperationResultCode.CART_NOT_EXIST.getDescription() + " cart id: " + identifier));
        when(cartService.updateCartItems(items)).thenReturn(items);
        mockMvc.perform(put("/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Update-Cart-Item", "Cart-Item-Not-Exist"));
    }

    @Test
    public void updateCartItemWhenIdentifierIsNullThrowException() throws Exception {
        CreateCartItemReq req = new CreateCartItemReq();
        UpdateCartItemRsp rsp = new UpdateCartItemRsp();
        UUID identifier = UUID.randomUUID();
        rsp.setCartId(identifier);
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setIdentifier(null);
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        when(cartService.get(identifier)).thenReturn(cart);
        when(cartService.updateCartItems(items)).thenReturn(items);
        mockMvc.perform(put("/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Update-Cart-Item", "Identifier-Is-Null"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("cartItem's identifier can not be null"));
    }

    @Test
    public void updateCartItemWhenCartItemIsNotBelongCartThrowException() throws Exception {
        CreateCartItemReq req = new CreateCartItemReq();
        UpdateCartItemRsp rsp = new UpdateCartItemRsp();
        UUID identifier = UUID.randomUUID();
        rsp.setCartId(identifier);
        Cart cart = CartDataFactory.cart();
        CartItem cartItem1 = CartDataFactory.cartItem();
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
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem1)));
        when(cartService.get(identifier)).thenReturn(cart);
        when(cartService.updateCartItems(items)).thenReturn(items);
        mockMvc.perform(put("/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Update-Cart-Item", "Cart-Item-Is-Not-Belong-Cart"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("cart item not exist: " + cartItem1.getIdentifier().toString()));
    }

    @Test
    public void updateCartItemWhenBookingRangeIsInvalidThrowException() throws Exception {
        CreateCartItemReq req = new CreateCartItemReq();
        UpdateCartItemRsp rsp = new UpdateCartItemRsp();
        UUID identifier = UUID.randomUUID();
        rsp.setCartId(identifier);
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.getBookingRange().setLower(Instant.parse("2019-11-11T00:00:00Z"));
        cartItem.getBookingRange().setUpper(Instant.parse("2019-11-10T00:00:00Z"));
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        when(cartService.get(identifier)).thenReturn(cart);
        when(cartService.updateCartItems(items)).thenReturn(items);
        mockMvc.perform(put("/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Update-Cart-Item", "Booking-Range-Is-Invalid"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("invalid parameters. booking range"));
    }

    @Test
    public void updateCartItemWhenTrimmedBookingRangeIsInvalidThrowException() throws Exception {
        CreateCartItemReq req = new CreateCartItemReq();
        UpdateCartItemRsp rsp = new UpdateCartItemRsp();
        UUID identifier = UUID.randomUUID();
        rsp.setCartId(identifier);
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.getTrimmedBookingRange().setLower(Instant.parse("2019-11-11T00:00:00Z"));
        cartItem.getTrimmedBookingRange().setUpper(Instant.parse("2019-11-10T00:00:00Z"));
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        when(cartService.get(identifier)).thenReturn(cart);
        when(cartService.updateCartItems(items)).thenReturn(items);
        mockMvc.perform(put("/carts/{cart-id}/items", identifier)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newErrorDocument("Cart-Item", "Update-Cart-Item", "Trimmed-Booking-Range-Is-Invalid"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.errorMsg").value("invalid parameters. trimmed booking range"));
    }

    @Test
    public void updateCartItemWhenBookingRangeIsInvalidThrowException1() throws Exception {
        CreateCartItemReq req = new CreateCartItemReq();
        UpdateCartItemRsp rsp = new UpdateCartItemRsp();
        UUID identifier = UUID.randomUUID();
        rsp.setCartId(identifier);
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setBookingRange(null);
        cartItem.setTrimmedBookingRange(null);
        List<CartItem> items = new ArrayList<>();
        items.add(cartItem);
        cart.setItems(items);
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        when(cartService.get(identifier)).thenReturn(cart);
        when(cartService.updateCartItems(items)).thenReturn(items);
        mockMvc.perform(put("/carts/{cart-id}/items", identifier)
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk());
    }
}
