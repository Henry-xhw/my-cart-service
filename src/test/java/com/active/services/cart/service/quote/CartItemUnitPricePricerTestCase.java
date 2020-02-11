package com.active.services.cart.service.quote;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RunWith(SpringRunner.class)
public class CartItemUnitPricePricerTestCase {

    private CartItemUnitPricePricer cartItemUnitPricePricer;

    @Before
    public void setUp() {
        Map<Long, FeeDto> feeDtoHashMap = new HashMap<>();
        FeeDto mock = new FeeDto();
        mock.setAmount(BigDecimal.valueOf(5));
        mock.setName("name");
        mock.setDescription("description");
        feeDtoHashMap.put(1L, mock);
        cartItemUnitPricePricer = new CartItemUnitPricePricer(feeDtoHashMap);
    }

    @Test
    public void quoteUnitPriceIsNull() {
        CartQuoteContext cartQuoteContext = buildCartQuoteContext();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setUnitPrice(null);
        cartItem.setProductId(1L);
        cartItem.setFees(new ArrayList<>());
        cartItemUnitPricePricer.quote(cartQuoteContext, cartItem);
        Assert.assertEquals(cartItem.getFees().get(0).getUnitPrice(), BigDecimal.valueOf(5));
    }

    @Test
    public void quoteUnitPriceIsNotNull() {
        CartQuoteContext cartQuoteContext = buildCartQuoteContext();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItemUnitPricePricer.quote(cartQuoteContext, cartItem);
        Assert.assertEquals(cartItem.getUnitPrice(), BigDecimal.valueOf(10));
    }

    private CartQuoteContext buildCartQuoteContext() {
        Cart cart = CartDataFactory.cart();
        cart.setIdentifier(UUID.randomUUID());
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setFees(new ArrayList<>());
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setItems(cartItems);
        return new CartQuoteContext(cart);
    }
}
