package com.active.services.cart.service.validator;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
import com.active.services.domain.dto.ProductDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/**
 * 1. Product exists
 * 2. Product currency has currency found
 *
 */
@RequiredArgsConstructor
@Slf4j
public class CartItemsProductValidator {
    private final List<CartItem> cartItems;

    private final List<ProductDto> products;

    public void validate() {
        Map<Long, ProductDto> foundProductById = products.stream()
                .collect(toMap(ProductDto::getId, Function.identity()));

        StringBuilder msg = new StringBuilder();
        msg.append("Product not found: ");
        boolean anyProductNotFound = false;

        StringBuilder noCurrencyMsg = new StringBuilder();
        noCurrencyMsg.append("Product currency not found: ");
        boolean anyProductCurrencyNotFound = false;
        for (CartItem cartItem : cartItems) {
            ProductDto foundProduct = foundProductById.get(cartItem.getProductId());
            if (foundProduct == null) {
                anyProductNotFound = true;
                msg.append(cartItem.getProductId());
            } else if (StringUtils.isEmpty(foundProduct.getCurrency())) {
                anyProductCurrencyNotFound = true;
                noCurrencyMsg.append(cartItem.getProductId());
            }
        }

        if (anyProductNotFound) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, msg.toString());
        }

        if (anyProductCurrencyNotFound) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, noCurrencyMsg.toString());
        }
    }
}
