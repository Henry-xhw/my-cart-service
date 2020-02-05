package com.active.services.cart.service.validator;

import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.domain.dto.ProductDto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.active.services.cart.domain.Cart.flattenCartItems;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateCartItemsValidator {

    private final Cart cart;

    private final List<CartItem> cartItems;

    private final ProductServiceSoap productServiceSoap;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CreateCartItemsValidator(Cart cart, List<CartItem> cartItems, ProductServiceSoap productServiceSoap) {
        this.cart = cart;
        this.cartItems = flattenCartItems(cartItems);
        this.productServiceSoap = productServiceSoap;
    }

    public void validate() {
        if (CollectionUtils.isEmpty(cartItems)) {
            return;
        }

        new CartItemsIdentifierValidator(cart, cartItems).validate();

        List<CartItem> newCartItems = cartItems.stream().filter(item -> item.getIdentifier() == null)
                .collect(Collectors.toList());
        if (!newCartItems.isEmpty()) {
            List<ProductDto> foundProducts = emptyIfNull(productServiceSoap.getProducts(newCartItems));
            new CartItemsProductValidator(newCartItems, foundProducts).validate();
            new CartItemsCurrencyValidator(cart, foundProducts).validate();
        }
    }
}
