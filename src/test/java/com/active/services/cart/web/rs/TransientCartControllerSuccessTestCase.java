package com.active.services.cart.web.rs;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static net.minidev.json.JSONValue.toJSONString;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.checkerframework.checker.units.qual.C;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.active.services.cart.application.CartService;
import com.active.services.cart.model.BookingDuration;
import com.active.services.cart.model.CartDto;
import com.active.services.cart.model.CartItemDto;
import com.active.services.cart.model.CartItemFacts;
import com.active.services.cart.model.CartItemOption;
import com.active.services.cart.model.CreateCartsReq;
import com.active.services.cart.model.FactKVPair;

@RunWith(MockitoJUnitRunner.class)
public class TransientCartControllerSuccessTestCase {
    public static final String CONTENT_TYPE = "application/vnd.active.cart-service.v1+json";
    @Rule
    public JUnitRestDocumentation documentation = new JUnitRestDocumentation();
    private MockMvc mockMvc;
    @Mock
    private CartService cartService;
    private TransientCartController transientCartController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        transientCartController = new TransientCartController(cartService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(transientCartController)
            .apply(documentationConfiguration(this.documentation)
                .operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint()))
            .build();
    }

    @Test
    public void createCartsWithValidReq() throws Exception {
        List<CartDto> carts = new ArrayList<>();

        CartDto cartDto = CartDto.builder().identifier(UUID.randomUUID().toString()).currency("USD").cartItemDtos(buildCartItemDtos()).orgIdentifier("3456").build();
        carts.add(cartDto);
        CreateCartsReq req = new CreateCartsReq(new ArrayList<CartDto> (carts));

        this.mockMvc.perform(post("/api/cart/transient/carts")
            .contentType(CONTENT_TYPE).content(toJSONString(req)))
            .andExpect(status().isOk()).andDo(document("api-cart-createcarts-success", resource("create carts success")));
    }

    private List<CartItemDto> buildCartItemDtos() {
        List<CartItemDto> cartItemDtos = new ArrayList<>();
        CartItemFacts cartItemFacts = new CartItemFacts();
        List<FactKVPair> factKVPairs = new ArrayList<>();
        FactKVPair factKVPair = new FactKVPair("age", 35);
        factKVPairs.add(factKVPair);
        cartItemFacts.setFactKVPairs(factKVPairs);
        CartItemOption cartItemOption = new CartItemOption();
        List<BookingDuration> bookingDurations = new ArrayList<>();
        BookingDuration bookingDuration =
            new BookingDuration(LocalDateTime.of(2019, 9, 12, 8, 0, 0), LocalDateTime.of(2019, 9, 12, 12, 0, 0));
        bookingDurations.add(bookingDuration);
        cartItemOption.setBookingDurations(bookingDurations);
        CartItemDto cartItemDto =
            CartItemDto.builder().identifier(UUID.randomUUID().toString()).productId(50L).quantity(2)
                .cartItemFacts(cartItemFacts).build();
        cartItemDtos.add(cartItemDto);
        return cartItemDtos;
    }


}
