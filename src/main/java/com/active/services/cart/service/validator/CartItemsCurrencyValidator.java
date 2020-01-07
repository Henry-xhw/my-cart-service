package com.active.services.cart.service.validator;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.model.ErrorCode;
import com.active.services.domain.dto.ProductDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;

@RequiredArgsConstructor
@Slf4j
public class CartItemsCurrencyValidator {
    private final Cart cart;

    private final List<ProductDto> products;

    public void validate() {
        Map<String, List<ProductDto>> productsByCurrency = products.stream()
                .collect(groupingBy(ProductDto::getCurrency));
        productsByCurrency.remove(cart.getCurrencyCode());
        if (!productsByCurrency.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append("Cart item currency code error: cart currency is - ");
            msg.append(cart.getCurrencyCode());
            msg.append(". Cart item currency -");
            productsByCurrency.forEach((productCurrency, products) -> {
                msg.append("[currency code ->");
                msg.append(productCurrency);
                msg.append(", product ids ->");
                msg.append(products.stream().map(p -> p.getId().toString()).collect(joining(",")));
                msg.append("]");
            });

            LOG.error(msg.toString());
            throw new CartException(ErrorCode.VALIDATION_ERROR, msg.toString());
        }
    }
}
