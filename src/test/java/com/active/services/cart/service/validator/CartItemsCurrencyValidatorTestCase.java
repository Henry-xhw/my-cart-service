package com.active.services.cart.service.validator;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.domain.dto.ProductDto;

import org.junit.Test;

import java.util.Arrays;

public class CartItemsCurrencyValidatorTestCase {

    @Test
    public void validatePass() {
        Cart cart = new Cart();
        cart.setCurrencyCode("USD");

        ProductDto productDto = new ProductDto();
        productDto.setCurrency("USD");

        new CartItemsCurrencyValidator(cart, Arrays.asList(productDto)).validate();
    }

    @Test(expected = CartException.class)
    public void validateFailed() {
        Cart cart = new Cart();
        cart.setCurrencyCode("USD");

        ProductDto productDto = new ProductDto();
        productDto.setId(1L);
        productDto.setCurrency("USD");

        ProductDto productDto2 = new ProductDto();
        productDto2.setId(2L);
        productDto2.setCurrency("AUD");

        new CartItemsCurrencyValidator(cart, Arrays.asList(productDto, productDto2)).validate();
    }
}
