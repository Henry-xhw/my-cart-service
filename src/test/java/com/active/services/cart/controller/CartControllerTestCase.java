package com.active.services.cart.controller;

import com.active.services.cart.common.CartException;
import com.active.services.cart.controller.v1.CartController;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.mock.MockCart;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.req.CreateCartReq;
import com.active.services.cart.model.v1.req.QuoteReq;
import com.active.services.cart.model.v1.req.UpdateCartReq;
import com.active.services.cart.model.v1.rsp.CreateCartRsp;
import com.active.services.cart.model.v1.rsp.FindCartByIdRsp;
import com.active.services.cart.model.v1.rsp.QuoteRsp;
import com.active.services.cart.model.v1.rsp.SearchCartRsp;
import com.active.services.cart.service.CartService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.active.services.cart.controller.Constants.V1_MEDIA;
import static com.active.services.cart.restdocs.RestDocument.autoPathParameterDoc;
import static com.active.services.cart.restdocs.RestDocument.autoRelaxedResponseFieldsDoc;
import static com.active.services.cart.restdocs.RestDocument.autoRequestFieldsDoc;
import static com.active.services.cart.restdocs.RestDocument.autoResponseFieldsDoc;
import static com.active.services.cart.restdocs.RestDocument.newErrorDocument;
import static com.active.services.cart.restdocs.RestDocument.newSuccessDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CartController.class, secure = false)
public class CartControllerTestCase extends BaseControllerTestCase {
    
    private UUID cartId = UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D");

    @MockBean
    private CartService cartService;

    @Test
    public void createCartSuccess() throws Exception {
        CreateCartReq req = new CreateCartReq();
        CartDto cartDtoReq = MockCart.mockCartDto();
        req.setOwnerId(cartDtoReq.getOwnerId());
        req.setKeyerId(cartDtoReq.getKeyerId());
        req.setCurrencyCode(cartDtoReq.getCurrencyCode());
        req.setReservationGroupId(UUID.randomUUID());
        req.setCouponCodes(cartDtoReq.getCouponCodes());
        req.setSalesChannel("Channel");
        CreateCartRsp rsp = new CreateCartRsp();
        rsp.setCart(cartDtoReq);
        //req.setIdentifier(cartDtoReq.getIdentifier());
        doNothing().when(cartService).create(any());

        String result = mockMvc.perform(
          post("/carts").contentType(V1_MEDIA).accept(V1_MEDIA).headers(actorIdHeader())
            .content(objectMapper.writeValueAsString(req))).andExpect(status().isOk()).andDo(
          newSuccessDocument("Cart", "Create-Cart",
            autoRequestFieldsDoc(req)))
          .andReturn().getResponse()
          .getContentAsString();
        CreateCartReq resultRsp = objectMapper.readValue(result, CreateCartReq.class);
        verify(cartService, times(1)).create(any());
        Assert.assertNotNull(resultRsp);
    }

    @Test
    public void createCartWhenOwnerIdIsNullThrowException() throws Exception {
        CreateCartReq req = new CreateCartReq();
        CartDto cartDtoReq = MockCart.mockCartDto();
        req.setOwnerId(null);
        req.setKeyerId(cartDtoReq.getKeyerId());
        req.setCurrencyCode(cartDtoReq.getCurrencyCode());
        doNothing().when(cartService).create(any());
        mockMvc.perform(
            post("/carts").contentType(V1_MEDIA).accept(V1_MEDIA).headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req))).andExpect(status().isOk()).andDo(
            newErrorDocument("Cart", "Create-Cart", "Owner_Id_IS_Null"));
        verify(cartService, never()).create(any());
    }

    @Test
    public void createCartWhenKeyerIdIsNullThrowException() throws Exception {
        CreateCartReq req = new CreateCartReq();
        CartDto cartDtoReq = MockCart.mockCartDto();
        req.setOwnerId(cartDtoReq.getOwnerId());
        req.setKeyerId(null);
        req.setCurrencyCode(cartDtoReq.getCurrencyCode());
        doNothing().when(cartService).create(any());
        mockMvc.perform(
            post("/carts").contentType(V1_MEDIA).accept(V1_MEDIA).headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req))).andExpect(status().isOk()).andDo(
            newErrorDocument("Cart", "Create-Cart", "Keyer_Id_IS_Null"));
        verify(cartService, never()).create(any());
    }

    @Test
    public void createCartWhenCurrencyCodeIsNullThrowException() throws Exception {
        CreateCartReq req = new CreateCartReq();
        CartDto cartDtoReq = MockCart.mockCartDto();
        req.setOwnerId(cartDtoReq.getOwnerId());
        req.setKeyerId(cartDtoReq.getKeyerId());
        req.setCurrencyCode(null);
        doNothing().when(cartService).create(any());
        mockMvc.perform(
            post("/carts").contentType(V1_MEDIA).accept(V1_MEDIA).headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req))).andExpect(status().isOk()).andDo(
            newErrorDocument("Cart", "Create-Cart", "Currency_Code_IS_Null"));
        verify(cartService, never()).create(any());
    }

    @Test
    public void updateCartSuccess() throws Exception {
        UpdateCartReq req = new UpdateCartReq();
        req.setReservationGroupId(UUID.randomUUID());
        req.setCouponCodes(Collections.singleton("FDSAFSA"));
        req.setSalesChannel("Channel");
        doNothing().when(cartService).update(any());

        mockMvc.perform(put("/carts/{id}", cartId)
                .contentType(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newSuccessDocument("Cart", "Update-Cart",
                        autoRequestFieldsDoc(req),
                        pathParameters(autoPathParameterDoc("id", CartDto.class, "identifier"))));
        verify(cartService, times(1)).update(any());
    }

    @Test
    public void deleteCartSuccess() throws Exception {
        when(cartService.getCartByUuid(any(UUID.class))).thenReturn(CartDataFactory.cart());
        mockMvc.perform(delete("/carts/{id}", cartId)
          .headers(actorIdHeader())
          .contentType(V1_MEDIA))
          .andExpect(status().isOk())
          .andDo(newSuccessDocument("Cart", "Delete-Cart",
            pathParameters(autoPathParameterDoc("id", CartDto.class, "identifier"))));
    }

    @Test
    public void deleteCartWhenCartNotExistThrowException() throws Exception {
        when(cartService.getCartByUuid(any(UUID.class))).thenThrow(new CartException(ErrorCode.CART_NOT_FOUND, "cart " +
                "not found"));
        mockMvc.perform(delete("/carts/{id}", cartId)
          .headers(actorIdHeader())
          .contentType(V1_MEDIA))
          .andExpect(status().isOk())
          .andDo(newErrorDocument("Cart", "Delete-Cart", "Cart-Not-Exist"));
    }

    @Test
    public void findCartsByIdSuccess() throws Exception {
        FindCartByIdRsp rsp = new FindCartByIdRsp();
        rsp.setCart(MockCart.mockCartDto());
        UUID identifier = UUID.randomUUID();
        Cart cart = MockCart.mockCartDomain();
        when(cartService.getCartByUuid(identifier)).thenReturn(cart);
        try {
            String result = mockMvc.perform(get("/carts/{id}", identifier)
                    .contentType(V1_MEDIA).accept(V1_MEDIA)
                    .headers(actorIdHeader()))
                    .andExpect(status().isOk())
                    .andDo(newSuccessDocument("Cart", "Find-Cart-By-Id",
                            pathParameters(autoPathParameterDoc("id", CartDto.class, "identifier")),
                            autoResponseFieldsDoc(rsp)))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.cart.currencyCode").value("USD"))
                    .andReturn().getResponse().getContentAsString();
            CartDto cartDto = objectMapper.readValue(result, FindCartByIdRsp.class).getCart();
            Assert.assertNotNull(cartDto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void findCartsByOwnerIdSuccess() throws Exception {
        SearchCartRsp rsp = new SearchCartRsp();
        UUID ownerId = UUID.randomUUID();
        Cart cart1 = MockCart.mockCartDomain();
        Cart cart2 = CartDataFactory.cart();
        List<UUID> cartIds = new ArrayList<>();
        cartIds.add(cart1.getIdentifier());
        cartIds.add(cart2.getIdentifier());
        rsp.setCartIds(cartIds);
        when(cartService.search(ownerId)).thenReturn(cartIds);
        mockMvc.perform(get("/carts/owners/{owner-id}", ownerId)
                .contentType(V1_MEDIA).accept(V1_MEDIA)
                .headers(actorIdHeader()))
                .andExpect(status().isOk())
                .andDo(newSuccessDocument("Cart", "Find-Cart-By-OwnerId",
                        pathParameters(autoPathParameterDoc("owner-id", CartDto.class, "ownerId")),
                        autoResponseFieldsDoc(rsp)));
    }

    @Test
    public void quoteCartSuccess() throws Exception {
        QuoteReq req = new QuoteReq();
        req.setCartId(UUID.randomUUID());
        req.setAaMember(false);
        QuoteRsp rsp = new QuoteRsp();
        rsp.setCartDto(MockCart.mockQuoteCartDto());
        Cart cart = CartDataFactory.cart();
        cart.setIdentifier(cartId);
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setId(1L);
        CartItemFee cartItemFee = CartDataFactory.cartItemFee();
        cartItem.setFees(Arrays.asList(cartItemFee));
        cart.setItems(Arrays.asList(cartItem));
        when(cartService.quote(req.getCartId(), req.isAaMember())).thenReturn(cart);
        String result = mockMvc.perform(post("/carts/quote")
                .contentType(V1_MEDIA).accept(V1_MEDIA)
                .headers(actorIdHeader())
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andDo(newSuccessDocument("Cart", "Quote-Process",
                        autoRequestFieldsDoc(req),
                        autoRelaxedResponseFieldsDoc(rsp)))
                .andReturn().getResponse().getContentAsString();
        QuoteRsp resultRsp = objectMapper.readValue(result, QuoteRsp.class);
        Assert.assertNotNull(resultRsp);
    }
}
