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

        validateFeeAllocation(context, cart);
    }

    private void validateFeeAllocation(CheckoutContext context, Cart cart) {
        if (CollectionUtils.isEmpty(context.getFeeAllocations())) {
            return;
        }

        Map<UUID, List<CartItemFeeAllocation>> feeAllocationMap = context.getFeeAllocations().stream()
                .collect(Collectors.groupingBy(CartItemFeeAllocation::getCartItemFeeIdentifier));
        feeAllocationMap.forEach((key, values) -> {
            if (values.size() > 1) {
                throw new CartException(ErrorCode.VALIDATION_ERROR, "cartItemFeeAllocation {0} is not unique.", key);
            }
        });

        List<CartItemFee> cartItemFees = cart.getFlattenCartItems().stream()
                .filter(item -> Objects.nonNull(item.getFees())).map(CartItem::getFlattenCartItemFees)
                .flatMap(List::stream).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cartItemFees)) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, "Not found cart item fees.");
        }

        if (context.getFeeAllocations().size() != cartItemFees.size()) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, "feeAllocations must include all cart item fees.");
        }


        if (isInvalidAllocation(cartItemFees, context.getFeeAllocations())) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, "Invalid allocation amount.");
        }
    }

    private boolean isInvalidAllocation(List<CartItemFee> cartItemFees, List<CartItemFeeAllocation> feeAllocations) {
        Map<UUID, CartItemFeeAllocation> feeAllocationMap = feeAllocations.stream()
                .collect(Collectors.toMap(CartItemFeeAllocation::getCartItemFeeIdentifier, f -> f));

        return cartItemFees.stream().anyMatch(fee -> !feeAllocationMap.containsKey(fee.getIdentifier()) ||
                isInvalidAllocationAmount(feeAllocationMap.get(fee.getIdentifier()), fee));
    }

    private boolean isInvalidAllocationAmount(CartItemFeeAllocation allocation, CartItemFee fee) {
        return allocation.getAmount().compareTo(fee.getUnitPrice().multiply(BigDecimal.valueOf(fee.getUnits()))) > 0;
    }
}
