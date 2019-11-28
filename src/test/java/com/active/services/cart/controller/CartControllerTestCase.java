package com.active.services.cart.controller;

import com.active.services.cart.controller.v1.CartController;
import com.active.services.cart.domain.Cart;
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

import java.util.UUID;

import static com.active.services.cart.restdocs.RestDocument.autoPathParameterDoc;
import static com.active.services.cart.restdocs.RestDocument.autoResponseFieldsDoc;
import static com.active.services.cart.restdocs.RestDocument.newSuccessDocument;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CartController.class, secure = false)
public class CartControllerTestCase extends BaseControllerTestCase {

    private static final String CONTENT_TYPE = "application/vnd.active.cart-service.v1+json";

    @MockBean
    private CartService cartService;

    @Test
    public void testFindCartsByIdWithValidReq() throws Exception {
        FindCartByIdRsp rsp = new FindCartByIdRsp();
        rsp.setCart(MockCart.mockCartDto());
        UUID identifier = UUID.randomUUID();
        Cart cart = MockCart.mockCartDomain();
        when(cartService.get(identifier)).thenReturn(cart);
        String result = mockMvc.perform(get("/carts/{id}", identifier)
                .contentType(CONTENT_TYPE).accept(CONTENT_TYPE)
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