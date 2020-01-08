package com.active.services.cart.service.quote;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.product.nextgen.v1.rsp.QuoteSingleRsp;

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
        BigDecimal unitPrice = new BigDecimal(1);
        Cart cart = CartDataFactory.cart();
        cart.setIdentifier(UUID.randomUUID());
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setFees(new ArrayList<>());
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setItems(cartItems);
        QuoteSingleRsp quoteSingleRsp = new QuoteSingleRsp();
        quoteSingleRsp.setAmount(unitPrice);
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
        QuoteSingleRsp quoteSingleRsp = new QuoteSingleRsp();
        quoteSingleRsp.setAmount(unitPrice);
        when(productService.quote(any())).thenReturn(quoteSingleRsp);
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        cartItemUnitPricePricer.quote(cartQuoteContext, cartItem);
        assertEquals(0, unitPrice.compareTo(cartQuoteContext.getCart().getItems().get(0).getFees().get(0).getUnitPrice()));
    }

    @Test(expected = CartException.class)
    public void quoteCallProductServiceResultSuccessFalse() {
        QuoteSingleRsp quoteSingleRsp = new QuoteSingleRsp();
        quoteSingleRsp.setSuccess(false);
        when(productService.quote(any())).thenReturn(quoteSingleRsp);
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setUnitPrice(null);
        cartItemUnitPricePricer.quote(cartQuoteContext, cartItem);
    }

    @Test(expected = CartException.class)
    public void quoteCallProductServiceAmountNull() {
        QuoteSingleRsp quoteSingleRsp = new QuoteSingleRsp();
        quoteSingleRsp.setAmount(null);
        when(productService.quote(any())).thenReturn(quoteSingleRsp);
        Cart cart = CartDataFactory.cart();
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setUnitPrice(null);
        cartItemUnitPricePricer.quote(cartQuoteContext, cartItem);
    }
}
