package com.active.services.cart.service.validator;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CartItemsIdentifierValidator {
    private final Cart cart;

    private final List<CartItem> cartItems;

    public void validate() {
        boolean anyIdentifierNotFound = false;
        StringBuilder msg = new StringBuilder();

        for (CartItem it : cartItems) {
            if (it.getIdentifier() != null) {
                if (!cart.findCartItem(it.getIdentifier()).isPresent()) {
                    anyIdentifierNotFound = true;
                    msg.append("cart item - ");
                    msg.append(it.getIdentifier());
                    msg.append(" does not exist.");
                }
            }
        }
        if (anyIdentifierNotFound) {
            LOG.error(msg.toString());
            throw new CartException(ErrorCode.VALIDATION_ERROR, msg.toString());
        }
    }
}
