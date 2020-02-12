package com.active.services.cart.service.validator;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.CartItem;
import com.active.services.domain.dto.ProductDto;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CartItemsProductValidatorTestCase {
    @Test
    public void validatePass() {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setCurrency("USD");

        new CartItemsProductValidator(Arrays.asList(cartItem), Arrays.asList(productDto)).validate();
    }

    @Test(expected = CartException.class)
    public void validateProductExists() {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);

        new CartItemsProductValidator(Arrays.asList(cartItem), new ArrayList<>()).validate();
    }

    @Test(expected = CartException.class)
    public void validateCurrencyNotFound() {
        CartItem cartItem = new CartItem();
        cartItem.setProductId(1L);

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);

        new CartItemsProductValidator(Arrays.asList(cartItem), Arrays.asList(productDto)).validate();
    }
}
