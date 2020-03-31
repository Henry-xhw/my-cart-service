package com.active.services.cart.service.quote.contract;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.contract.controller.v1.FeeAmountResult;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

        Optional<CartItemFee> priceFee = cartItem.getPriceCartItemFee();

        priceFee.ifPresent(itemFee -> emptyIfNull(feeAmountResults).stream().filter(Objects::nonNull).forEach(
            feeAmountResult -> {
                // build cartItemFee and add cart item fee
                itemFee.getSubItems().add(CartItemFeeBuilder.buildActiveFeeCartItemFee(cartItem.getQuantity(), feeAmountResult));
            }
        ));
    }
}
