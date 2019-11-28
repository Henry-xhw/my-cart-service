package com.active.services.cart.controller;

import com.active.services.cart.controller.v1.CartItemController;
import com.active.services.cart.controller.v1.CartMapper;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.req.CreateCartItemReq;
import com.active.services.cart.model.v1.rsp.CreateCartItemRsp;
import com.active.services.cart.model.v1.rsp.UpdateCartItemRsp;
import com.active.services.cart.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CartItemController.class, secure = false)
public class CartItemControllerTestCase extends BaseControllerTestCase {

    @MockBean
    private CartService cartService;

    @Test
    public void TestCreateCartItemSuccess() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(CartDataFactory.cart());
        CreateCartItemReq req = new CreateCartItemReq();
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(CartDataFactory.cartItem())));
        CreateCartItemRsp rsp = new CreateCartItemRsp();
        rsp.setCartId(UUID.randomUUID());
        mockMvc.perform(post("/carts/{cart-id}/items", UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D"))
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
    public void TestCreateCartItemFailWithCartNotExist() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(null);
        CreateCartItemReq req = new CreateCartItemReq();
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(CartDataFactory.cartItem())));
        mockMvc.perform(post("/carts/{cart-id}/items", UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D"))
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk())
          .andDo(newErrorDocument("Cart-Item", "Create-Cart-Item", "Cart-Item-Not-Exist"));
    }

    @Test
    public void TestCreateCartItemFailWithInvalidBookingRange() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(CartDataFactory.cart());
        CreateCartItemReq req = new CreateCartItemReq();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.getBookingRange().setLower(cartItem.getBookingRange().getUpper().plusMillis(1000L));
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        mockMvc.perform(post("/carts/{cart-id}/items", UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D"))
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk())
          .andDo(newErrorDocument("Cart-Item", "Create-Cart-Item", "Invalid-Booking-Range"));
    }

    @Test
    public void TestCreateCartItemFailWithInvalidTrimmedBookingRange() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(CartDataFactory.cart());
        CreateCartItemReq req = new CreateCartItemReq();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.getTrimmedBookingRange().setLower(cartItem.getTrimmedBookingRange().getUpper().plusMillis(1000L));
        req.setItems(Collections.singletonList(CartMapper.INSTANCE.toDto(cartItem)));
        mockMvc.perform(post("/carts/{cart-id}/items", UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D"))
          .contentType(V1_MEDIA)
          .headers(actorIdHeader())
          .content(objectMapper.writeValueAsString(req)))
          .andExpect(status().isOk())
          .andDo(newErrorDocument("Cart-Item", "Create-Cart-Item", "Invalid-Trimmed-Booking-Range"));
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
}
