package com.active.services.cart.web.rs;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import com.active.services.cart.model.CartItemFee;
import com.active.services.cart.model.CartItemFeeResult;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.CartItemOption;
import com.active.services.cart.model.CartItemResult;
import com.active.services.cart.model.CartResult;
import com.active.services.cart.model.FactKVPair;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.util.JacksonUtils;

@RunWith(MockitoJUnitRunner.class)
public class PersistedCartControllerTestCase {
    public static final String CONTENT_TYPE = "application/vnd.active.cart-service.v1+json";
    @Rule
    public JUnitRestDocumentation documentation = new JUnitRestDocumentation();
    private MockMvc mockMvc;
    @Mock
    private CartService cartService;
    private PersistedCartController controller;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        controller = new PersistedCartController(cartService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
            .apply(documentationConfiguration(this.documentation)
                .operationPreprocessors()
                .withRequestDefaults(prettyPrint())
                .withResponseDefaults(prettyPrint()))
            .build();
    }

    @Test
    public void createCart() throws Exception {
        CartDto cart = new CartDto();
        List<CartItemDto> items = new ArrayList<>();
        CartItemDto item = buildCartItemDto();
        items.add(item);
        cart.setReferenceId("reference-id");
        cart.setCurrency("USD");
        cart.setCartItemDtos(items);
        cart.setPriceDate(LocalDateTime.now());

        CartResult cartResult = buildCartResult(cart);
        List<CartItemResult> itemResults = new ArrayList<>();
        CartItemResult itemResult = buildCartItemResult(item);
        itemResults.add(itemResult);
        cartResult.setCartItemResults(itemResults);

        Mockito.doReturn(cartResult).when(cartService).createCart(cart);
        System.out.println(JacksonUtils.writeValueAsString(cart));
        CartDto cart2 = new CartDto();
        cart2 = JacksonUtils.readValue(JacksonUtils.writeValueAsString(cart), cart2.getClass());
        this.mockMvc.perform(post("/api/carts")
            .contentType(CONTENT_TYPE).content(JacksonUtils.writeValueAsString(cart)))
            .andExpect(status().isOk()).andDo(document("api-cart-createcarts-success", resource("create carts success")));

    }

    private CartItemDto buildCartItemDto() {

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
            CartItemDto.builder().referenceId("reference-id").orgIdentifier("agency-id").productId(50L).quantity(2).option(cartItemOption).priceOverride(new BigDecimal("50.00"))
                .cartItemFacts(cartItemFacts).build();
        return cartItemDto;
    }

    private CartItemResult buildCartItemResult(CartItemDto cartItemDto) {
        CartItemResult cartItemResult = CartItemResult.builder().identifier(UUID.randomUUID()).paymentOptionAvailable(false).build();

        List<CartItemFeeResult> cartItemFeeResults = new ArrayList<>();
        CartItemFee cartItemFee = CartItemFee.builder().id(RandomUtils.nextLong()).name("product processing fee").description("product processing fee")
            .feeType(CartItemFeeType.PRICE).transactionType(FeeTransactionType.DEBIT).cartItemFeeOrigin("ACL").unitPrice(new BigDecimal("100.00")).units(2).subtotal(new BigDecimal("200.00")).build();

        CartItemFeeResult cartItemFeeResult = CartItemFeeResult.builder().name(cartItemFee.getName()).description(cartItemFee.getDescription())
            .feeType(cartItemFee.getFeeType()).transactionType(cartItemFee.getTransactionType()).cartItemFeeOrigin(cartItemFee.getCartItemFeeOrigin())
            .unitPrice(cartItemFee.getUnitPrice()).units(cartItemFee.getUnits()).subtotal(cartItemFee.getSubtotal()).build();
        cartItemFeeResults.add(cartItemFeeResult);
        cartItemResult.setCartItemFeeResults(cartItemFeeResults);

        cartItemResult.setItemTotal(new BigDecimal("200.00"));
        cartItemResult.setFeeTotal(new BigDecimal("200.00"));
        cartItemResult.setTaxTotal(new BigDecimal("20.00"));

        return cartItemResult;
    }

    private CartResult buildCartResult(CartDto cartDto) {
        CartResult cartResult = CartResult.builder().identifier(UUID.randomUUID()).currency(cartDto.getCurrency())
            .priceDate(cartDto.getPriceDate()).build();
        cartResult.setSubtotal(new BigDecimal("200.00"));
        cartResult.setFeeTotal(new BigDecimal("200.00"));
        cartResult.setTaxTotal(new BigDecimal("20.00"));
        return cartResult;
    }
}
