package com.active.services.cart.service.quote.price;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemUnitPricePricer implements CartItemPricer {

    private final Map<Long, FeeDto> feeDtoMap;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {

        if (cartItem.getOverridePrice() != null) {
            // has override price in CartItem
            cartItem.setGrossPrice(cartItem.getOverridePrice());
            cartItem.getFees().add(CartItemFeeBuilder.buildOverridePriceItemFee(cartItem));
            return;
        }

        FeeDto feeDto = feeDtoMap.get(cartItem.getProductId());
        cartItem.setGrossPrice(feeDto.getAmount());
        // no override price in CartItem
        cartItem.getFees().add(CartItemFeeBuilder.buildPriceItemFee(cartItem.getQuantity(), feeDto));
    }
}
