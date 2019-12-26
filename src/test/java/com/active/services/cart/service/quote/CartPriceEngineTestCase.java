package com.active.services.cart.service.quote;

import com.active.services.cart.CartServiceApp;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={CartServiceApp.class})
public class CartPriceEngineTestCase {

    private UUID cartId = UUID.fromString("BA5ED9E7-A2F2-F24B-CDA4-6399D76F0D4D");

    @Autowired
    private CartPriceEngine cartPriceEngine;

    @Test
    public void createCartItemsSuccess() {
        Cart cart = CartDataFactory.cart();
        cart.setIdentifier(cartId);
        CartItem cartItem = CartDataFactory.cartItem();
        ArrayList<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cart.setItems(cartItems);
        CartQuoteContext cartQuoteContext = new CartQuoteContext(cart);
        this.cartPriceEngine.quote(cartQuoteContext);
        Assert.assertEquals(cartQuoteContext.getCart().getItems().get(0).getUnitPrice(),
                cartQuoteContext.getCart().getItems().get(0).getFees().get(0).getUnitPrice());
    }

}
