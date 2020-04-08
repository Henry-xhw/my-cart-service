package com.active.services.cart.service.validator;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.AdHocDiscount;
import com.active.services.cart.domain.CartItem;

public class CartItemsCurrencyFormatValidatorTestCase {

    @Test
    public void validatePass() {
        String currencyCode = "USD";
        CartItem cartItem1 = new CartItem();
        cartItem1.setOverridePrice(new BigDecimal("11.22"));
        CartItem cartItem2 = new CartItem();
        cartItem2.setOverridePrice(new BigDecimal("11"));
        CartItem cartItem3 = new CartItem();
        cartItem3.setOverridePrice(new BigDecimal("0.11"));
        CartItem cartItem4 = new CartItem();
        cartItem4.setOverridePrice(null);
        
        AdHocDiscount adHocDiscount1 = new AdHocDiscount();
        adHocDiscount1.setDiscountAmount(new BigDecimal("11.22"));
        AdHocDiscount adHocDiscount2 = new AdHocDiscount();
        adHocDiscount2.setDiscountAmount(new BigDecimal("11"));
        AdHocDiscount adHocDiscount3 = new AdHocDiscount();
        adHocDiscount3.setDiscountAmount(new BigDecimal("0.22"));
        AdHocDiscount adHocDiscount4 = new AdHocDiscount();
        adHocDiscount4.setDiscountAmount(null);

        cartItem1.setAdHocDiscounts(Arrays.asList(adHocDiscount1, null, adHocDiscount2, adHocDiscount3, adHocDiscount4));

        new CartItemsCurrencyFormatValidator(currencyCode,
            Arrays.asList(cartItem1, null, cartItem2, cartItem3, cartItem4)).validate();
    }

    @Test
    public void validateNullPass() {
        new CartItemsCurrencyFormatValidator("USD", null).validate();
    }
    
    @Test
    public void validateWithoutAdHocDiscountPass() {
        String currencyCode = "USD";
        CartItem cartItem1 = new CartItem();
        cartItem1.setOverridePrice(new BigDecimal("11.22"));
        CartItem cartItem2 = new CartItem();
        cartItem2.setOverridePrice(new BigDecimal("11"));
        CartItem cartItem3 = new CartItem();
        cartItem3.setOverridePrice(new BigDecimal("0.11"));
        CartItem cartItem4 = new CartItem();
        cartItem4.setOverridePrice(null);

        new CartItemsCurrencyFormatValidator(currencyCode,
            Arrays.asList(cartItem1, null, cartItem2, cartItem3, cartItem4)).validate();
    }

    @Test(expected = CartException.class)
    public void validateOverridePriceFailed() {
        String currencyCode = "JPY";
        CartItem cartItem = new CartItem();
        cartItem.setOverridePrice(new BigDecimal("11.22"));

        new CartItemsCurrencyFormatValidator(currencyCode, Arrays.asList(cartItem)).validate();
    }

    @Test(expected = CartException.class)
    public void validateDiscountAmountFailed() {
        String currencyCode = "JPY";
        CartItem cartItem = new CartItem();
        cartItem.setOverridePrice(new BigDecimal("11"));
        
        AdHocDiscount adHocDiscount1 = new AdHocDiscount();
        adHocDiscount1.setDiscountAmount(new BigDecimal("11.22"));

        cartItem.setAdHocDiscounts(Arrays.asList(adHocDiscount1));

        new CartItemsCurrencyFormatValidator(currencyCode, Arrays.asList(cartItem)).validate();
    }
}
