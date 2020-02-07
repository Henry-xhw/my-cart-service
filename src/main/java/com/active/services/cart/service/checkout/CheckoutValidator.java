package com.active.services.cart.service.checkout;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeAllocation;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.service.CartStatus;

import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

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

        validateAllocation(context, cart);
    }

    private void validateAllocation(CheckoutContext context, Cart cart) {
        if (CollectionUtils.isEmpty(context.getFeeAllocations())) {
            return;
        }

        Map<UUID, BigDecimal> dueAmountMap = cart.getFlattenCartItems().stream()
            .filter(item -> !Objects.isNull(item.getFees())).map(CartItem::getFlattenCartItemFees)
            .flatMap(List::stream).collect(Collectors.toMap(CartItemFee::getIdentifier, CartItemFee::getDueAmount));
        if (isInvalidAllocation(context.getFeeAllocations(), dueAmountMap)) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, "Invalid allocation amount.");
        }
    }

    private boolean isInvalidAllocation(List<CartItemFeeAllocation> allocations, Map<UUID, BigDecimal> dueAmountMap) {
        return allocations.stream().anyMatch(alc -> dueAmountMap.containsKey(alc.getCartItemFeeIdentifier())
            && alc.getAmount().compareTo(dueAmountMap.get(alc.getCartItemFeeIdentifier())) != 0);
    }
}
