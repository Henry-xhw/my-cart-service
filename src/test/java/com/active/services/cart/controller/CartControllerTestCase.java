package com.active.services.cart.controller;

import com.active.services.cart.controller.v1.CartController;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.model.v1.CartDto;
import com.active.services.cart.service.CartService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static com.active.services.cart.controller.v1.Constants.V1_MEDIA;
import static com.active.services.cart.restdocs.RestDocument.autoPathParameterDoc;
import static com.active.services.cart.restdocs.RestDocument.newErrorDocument;
import static com.active.services.cart.restdocs.RestDocument.newSuccessDocument;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = CartController.class, secure = false)
public class CartControllerTestCase extends BaseControllerTestCase {

    @MockBean
    private CartService cartService;

    @Test
    public void TestCreateCartSuccess() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(CartDataFactory.cart());
        mockMvc.perform(delete("/carts/{id}", UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D"))
          .headers(actorIdHeader())
          .contentType(V1_MEDIA))
          .andExpect(status().isOk())
          .andDo(newSuccessDocument("Cart", "Delete-Cart",
            pathParameters(autoPathParameterDoc("id", CartDto.class, "identifier"))));
    }

    @Test
    public void TestCreateCartFailWithCartNotExist() throws Exception {
        when(cartService.get(any(UUID.class))).thenReturn(null);
        mockMvc.perform(delete("/carts/{id}", UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D"))
          .headers(actorIdHeader())
          .contentType(V1_MEDIA))
          .andExpect(status().isOk())
          .andDo(newErrorDocument("Cart", "Delete-Cart", "Cart-Not-Exist",
            pathParameters(autoPathParameterDoc("id", CartDto.class, "identifier"))));
    }
}
