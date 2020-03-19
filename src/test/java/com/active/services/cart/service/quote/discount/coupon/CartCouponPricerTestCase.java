package com.active.services.cart.service.quote.discount.coupon;


import com.active.services.cart.CartServiceApp;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.active.services.cart.service.quote.discount.multi.pricer.MultiDiscountBasePricerTestCase.loadCartQuoteContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes={CartServiceApp.class})
public class CartCouponPricerTestCase {

    @Autowired
    private CartCouponPricer cartCouponPricer;

    List<CartItem> noneZeroItems;

    @Before
    public void setup(){
        noneZeroItems = new ArrayList<>();
        loadCartQuoteContext();
    }

    @Test
    public void doQuoteWhenCartItemCouponsEmpty(){
        CartItem item1 = CartDataFactory.getCartItem(1, BigDecimal.ZERO,"cart item1");
        CartItem item2 = CartDataFactory.getCartItem(1, BigDecimal.ZERO,"cart item2");
        noneZeroItems.add(item1);
        noneZeroItems.add(item2);
        cartCouponPricer.doQuote(CartQuoteContext.get(), noneZeroItems);
        Assert.assertTrue(noneZeroItems.stream().allMatch(item -> item.getFees().get(0).getSubItems().size() == 0));
    }
}
