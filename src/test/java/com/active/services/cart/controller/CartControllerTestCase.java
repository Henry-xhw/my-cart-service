package com.active.services.cart.controller;

import com.active.services.cart.common.OperationResultCode;
import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.controller.v1.CartController;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.mock.MockCart;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.model.v1.rsp.FindCartByIdRsp;
import com.active.services.cart.service.CartService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;
import java.util.UUID;

import static com.active.services.cart.controller.v1.Constants.V1_MEDIA;
import static com.active.services.cart.restdocs.RestDocument.autoPathParameterDoc;
import static com.active.services.cart.restdocs.RestDocument.autoResponseFieldsDoc;
import static com.active.services.cart.restdocs.RestDocument.newErrorDocument;
import static com.active.services.cart.restdocs.RestDocument.newSuccessDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CartController.class, secure = false)
public class CartControllerTestCase extends BaseControllerTestCase {

    private UUID cartId = UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D");

    @MockBean
    private CartService cartService;

    @Test
    public void deleteCartSuccess() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(CartDataFactory.cart());
        mockMvc.perform(delete("/carts/{id}", cartId)
          .headers(actorIdHeader())
          .contentType(V1_MEDIA))
          .andExpect(status().isOk())
          .andDo(newSuccessDocument("Cart", "Delete-Cart",
            pathParameters(autoPathParameterDoc("id", CartDto.class, "identifier"))));
    }

    @Test
    public void deleteCartWhenCartNotExistThrowException() throws Exception {
        when(cartService.get(any(UUID.class))).thenThrow(new CartException(OperationResultCode.CART_NOT_EXIST.getCode(),
            OperationResultCode.CART_NOT_EXIST.getDescription() + " cart id: " + cartId));
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
        when(cartService.get(identifier)).thenReturn(cart);
        String result = mockMvc.perform(get("/carts/{id}", identifier)
                .contentType(V1_MEDIA).accept(V1_MEDIA)
                .headers(actorIdHeader()))
                .andExpect(status().isOk())
                .andDo(newSuccessDocument("Cart", "Find-Cart",
                        pathParameters(autoPathParameterDoc("id", CartDto.class, "identifier")),
                        autoResponseFieldsDoc(rsp)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cart.currencyCode").value("USD"))
                .andReturn().getResponse().getContentAsString();
        CartDto cartDto = objectMapper.readValue(result, FindCartByIdRsp.class).getCart();
        Assert.assertNotNull(cartDto);
    }
}
