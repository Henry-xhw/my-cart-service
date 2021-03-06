package com.active.services.cart.service.checkout;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.service.CartStatus;
import org.apache.commons.collections4.CollectionUtils;

import java.util.UUID;

public class CheckoutValidator {

    public void validate(CheckoutContext context) {
        Cart cart = context.getCart();

        UUID cartId = cart.getIdentifier();
        if (CollectionUtils.isEmpty(cart.getFlattenCartItems())) {
            throw new CartException(ErrorCode.CART_ITEM_NOT_FOUND,
                    "There is no cart item for cartId: {0} ", cartId);
        }
        if (cart.getVersion() != cart.getPriceVersion()) {
            throw new CartException(ErrorCode.CART_PRICING_OUT_OF_DATE,
                    "Cart: {0} price had out of date. Price version : {1}, cart version: {2}. Please call quote " +
                            "before checkout.",
                    cartId, cart.getPriceVersion(), cart.getVersion());
        }
        if (cart.getCartStatus() == CartStatus.FINALIZED) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, "Cart already been finalized.");
        }

        boolean notFoundCartItemFees =
                cart.getItems().stream().anyMatch(item -> CollectionUtils.isEmpty(item.getFlattenCartItemFees()));
        if (notFoundCartItemFees) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, "There is no cart item fees for cartItem.");
        }

    }

}
