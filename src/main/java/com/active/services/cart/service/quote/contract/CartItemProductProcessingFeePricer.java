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
        emptyIfNull(feeAmountResults).stream().filter(Objects::nonNull).forEach(
            feeAmountResult -> {
                // build cartItemFee
                CartItemFee cartItemFee = new CartItemFee();
                cartItemFee.setIdentifier(UUID.randomUUID());
                cartItemFee.setDescription(feeAmountResult.getDescription());
                cartItemFee.setTransactionType(FeeTransactionType.DEBIT);
                cartItemFee.setUnitPrice(feeAmountResult.getAmount());
                cartItemFee.setUnits(cartItem.getQuantity());
                if (feeAmountResult.getFeeType() == FeeType.PERCENT) {
                    cartItemFee.setType(CartItemFeeType.PROCESSING_PERCENT);
                } else {
                    cartItemFee.setType(CartItemFeeType.PROCESSING_FLAT);
                }
                cartItem.getFees().add(cartItemFee);
            }
        );
    }
}
