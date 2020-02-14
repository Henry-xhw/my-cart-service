package com.active.services.cart.service.quote;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.price.CartItemUnitPricePricer;
import com.active.services.cart.service.quote.price.CartUnitPricePricer;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;
import com.active.services.product.nextgen.v1.rsp.QuoteRsp;

import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartUnitPricePricerTestCase {

    private CartUnitPricePricer cartUnitPricePricer;

    @Mock
    private ProductService productService;

    @Before
    public void setUp() {
        cartUnitPricePricer = spy(new CartUnitPricePricer(productService));
    }

    @Test
    public void quoteSuccess() {
        CartQuoteContext cartQuoteContext = buildCartQuoteContext();
        when(cartUnitPricePricer.getCartItemPricer(any())).thenReturn(mock(CartItemUnitPricePricer.class));
        QuoteRsp quoteRsp = new QuoteRsp();
        FeeDto feeDto = new FeeDto();
        feeDto.setAmount(BigDecimal.valueOf(5));
        feeDto.setName("name");
        feeDto.setDescription("description");
        quoteRsp.setFeeDtos(Arrays.asList(feeDto));
        quoteRsp.setSuccess(true);
        when(productService.quote(any())).thenReturn(quoteRsp);
        cartUnitPricePricer.quote(cartQuoteContext);
        Assert.assertEquals(cartQuoteContext.getCart().getItems().get(0).getFees().size(), 0);
    }

    @Test(expected = CartException.class)
    public void quoteFail() {
        CartQuoteContext cartQuoteContext = buildCartQuoteContext();
        when(cartUnitPricePricer.getCartItemPricer(any())).thenReturn(mock(CartItemUnitPricePricer.class));
        when(productService.quote(any())).thenReturn(mock(QuoteRsp.class));
        cartUnitPricePricer.quote(cartQuoteContext);
    }

    @NotNull
    private CartQuoteContext buildCartQuoteContext() {
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setUnitPrice(null);
        cartItem.setFees(new ArrayList<>());
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setItems(cartItems);
        return new CartQuoteContext(cart);
    }
}
