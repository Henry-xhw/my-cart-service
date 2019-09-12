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
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.CartItemOption;
import com.active.services.cart.model.CartItemResult;
import com.active.services.cart.model.CartResult;
import com.active.services.cart.model.CreateCartReq;
import com.active.services.cart.model.FactKVPair;
import com.active.services.cart.model.FeeTransactionType;

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

        List<CartItemDto> cartItemDtos = new ArrayList<>();
        CartItemDto cartItemDto = buildCartItemDto();
        cartItemDtos.add(cartItemDto);

        CartDto cartDto = CartDto.builder().identifier(UUID.randomUUID().toString()).currency("USD").cartItemDtos(cartItemDtos).orgIdentifier("3456").priceDate(LocalDateTime.now()).build();
        carts.add(cartDto);
        CreateCartReq req = new CreateCartReq(carts);

        List<CartResult> cartResults = new ArrayList<>();
        CartResult cartResult = buildCartResult(cartDto);

        List<CartItemResult> cartItemResults = new ArrayList<>();
        cartItemResults.add(buildCartItemResult(cartItemDto));
        cartResult.setCartItemResults(cartItemResults);
        cartResult.setSubtotal(cartItemResults.get(0).getItemTotal());
        cartResult.setFeeTotal(cartItemResults.get(0).getFeeTotal());
        cartResult.setTaxTotal(cartItemResults.get(0).getTaxTotal());

        cartResults.add(cartResult);
        Mockito.doReturn(cartResults).when(cartService).createCarts(carts);

        this.mockMvc.perform(post("/api/cart/transient/carts")
            .contentType(CONTENT_TYPE).content(toJSONString(req)))
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
            CartItemDto.builder().identifier(UUID.randomUUID().toString()).productId(50L).quantity(2).option(cartItemOption).priceOverride(new BigDecimal("50.00"))
                .cartItemFacts(cartItemFacts).build();
        return cartItemDto;
    }

    private CartItemResult buildCartItemResult(CartItemDto cartItemDto) {
        CartItemResult cartItemResult = CartItemResult.builder().identifier(cartItemDto.getIdentifier()).productId(cartItemDto.getProductId())
            .quantity(cartItemDto.getQuantity()).option(cartItemDto.getOption()).priceOverride(cartItemDto.getPriceOverride()).parentIdentifier(cartItemDto.getParentIdentifier())
            .cartItemFacts(cartItemDto.getCartItemFacts()).build();

        List<CartItemFee> cartItemFees = new ArrayList<>();
        CartItemFee cartItemFee = CartItemFee.builder().id(RandomUtils.nextLong()).name("product processing fee").description("product processing fee")
            .feeType(CartItemFeeType.PRICE).transactionType(FeeTransactionType.DEBIT).unitPrice(new BigDecimal("100.00")).units(2).subtotal(new BigDecimal("200.00")).build();
        cartItemFees.add(cartItemFee);

        cartItemResult.setCartItemFees(cartItemFees);

        cartItemResult.setItemTotal(new BigDecimal("200.00"));
        cartItemResult.setFeeTotal(new BigDecimal("200.00"));
        cartItemResult.setTaxTotal(new BigDecimal("20.00"));

        return cartItemResult;
    }

    private CartResult buildCartResult(CartDto cartDto) {
        CartResult cartResult = CartResult.builder().identifier(cartDto.getIdentifier()).currency(cartDto.getCurrency())
            .orgIdentifier(cartDto.getOrgIdentifier()).priceDate(cartDto.getPriceDate()).build();
        return cartResult;
    }

    @Test
    public void testLocalDateTime() {
        LocalDateTime test = LocalDateTime.now();
        String jsonStr = toJSONString(test);
        System.out.println(jsonStr);
    }


}
