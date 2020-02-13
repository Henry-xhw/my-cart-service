package com.active.services.cart.service.quote.contract;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.contract.controller.v1.FeeAmountResult;
import com.active.services.contract.controller.v1.type.FeeType;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author henryxu
 */
@Component
@Scope(value = SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemProductProcessingFeePricer implements CartItemPricer {
    private final List<FeeAmountResult> feeAmountResults;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {

        Optional<CartItemFee> priceFee = emptyIfNull(cartItem.getFees()).stream()
                .filter(CartItemProductProcessingFeePricer::isPriceType).findFirst();

        priceFee.ifPresent(itemFee -> emptyIfNull(feeAmountResults).stream().filter(Objects::nonNull).forEach(
            feeAmountResult -> {
                // build cartItemFee
                CartItemFee cartItemFee = new CartItemFee();
                cartItemFee.setIdentifier(UUID.randomUUID());
                cartItemFee.setDescription(feeAmountResult.getDescription());
                cartItemFee.setUnitPrice(feeAmountResult.getAmount());
                cartItemFee.setUnits(cartItem.getQuantity());
                mapTypesToCartItemFee(feeAmountResult.getFeeType(), cartItemFee);
                itemFee.getSubItems().add(cartItemFee);
            }
        ));

    }

    private void mapTypesToCartItemFee(FeeType feeType, CartItemFee cartItemFee) {
        if (feeType == FeeType.PERCENT) {
            cartItemFee.setType(CartItemFeeType.PROCESSING_PERCENT);
            cartItemFee.setTransactionType(FeeTransactionType.DEBIT);
        } else if (feeType == FeeType.FLAT_ADJUSTMENT_MAX) {
            cartItemFee.setType(CartItemFeeType.PROCESSING_FLAT);
            cartItemFee.setTransactionType(FeeTransactionType.CREDIT);
        } else {
            cartItemFee.setType(CartItemFeeType.PROCESSING_FLAT);
            cartItemFee.setTransactionType(FeeTransactionType.DEBIT);
        }
    }

    private static boolean isPriceType(CartItemFee cartItemFee) {
        return cartItemFee.getType() == CartItemFeeType.PRICE;
    }

}
