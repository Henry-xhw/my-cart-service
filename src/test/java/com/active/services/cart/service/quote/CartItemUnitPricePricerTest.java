package com.active.services.cart.service.quote;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;
import com.active.services.product.nextgen.v1.rsp.GetProductFeeRsp;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartItemUnitPricePricerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private CartItemUnitPricePricer cartItemUnitPricePricer;

    @Test
    public void quoteCartItemUnitPriceNotNull() {
        Cart cart = CartDataFactory.cart();
        cart.setIdentifier(UUID.randomUUID());
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setFees(new ArrayList<>());
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setItems(cartItems);
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        cartItemUnitPricePricer.quote(cartQuoteContext, cartItem);
        Assert.assertEquals(cartQuoteContext.getCart().getItems().get(0).getUnitPrice(),
                cartQuoteContext.getCart().getItems().get(0).getFees().get(0).getUnitPrice());
    }

    @Test
    public void quoteCartItemUnitPriceNull() {
        BigDecimal unitPrice = new BigDecimal(1);
        Cart cart = CartDataFactory.cart();
        cart.setIdentifier(UUID.randomUUID());
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setUnitPrice(null);
        cartItem.setFees(new ArrayList<>());
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setItems(cartItems);
        FeeDto feeDto = new FeeDto();
        feeDto.setAmount(unitPrice);
        GetProductFeeRsp getProductFeeRsp = new GetProductFeeRsp(feeDto);
        when(productService.quote(any())).thenReturn(getProductFeeRsp);
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        cartItemUnitPricePricer.quote(cartQuoteContext, cartItem);
        assertEquals(unitPrice, cartQuoteContext.getCart().getItems().get(0).getFees().get(0).getUnitPrice());
    }

    @Test(expected = CartException.class)
    public void quoteCallProductServiceResultSuccessFalse() {
        GetProductFeeRsp getProductFeeRsp = new GetProductFeeRsp(new FeeDto());
        getProductFeeRsp.setSuccess(false);
        when(productService.quote(any())).thenReturn(getProductFeeRsp);
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setUnitPrice(null);
        cartItemUnitPricePricer.quote(cartQuoteContext, cartItem);
    }

    @Test(expected = CartException.class)
    public void quoteCallProductServiceFeeDtoNull() {
        GetProductFeeRsp getProductFeeRsp = new GetProductFeeRsp(null);
        when(productService.quote(any())).thenReturn(getProductFeeRsp);
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setUnitPrice(null);
        cartItemUnitPricePricer.quote(cartQuoteContext, cartItem);
    }
}
