package com.active.services.cart.service.quote;

import java.util.UUID;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeTransactionType;
import com.active.services.cart.model.CartItemFeeType;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CartItemUnitPricePricer implements CartItemPricer {
    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
        CartItemFee unitPriceFee = new CartItemFee();

        unitPriceFee.setIdentifier(UUID.randomUUID());
        unitPriceFee.setDescription(cartItem.getProductDescription());
        unitPriceFee.setName(cartItem.getProductName());
        unitPriceFee.setTransactionType(CartItemFeeTransactionType.DEBIT);
        unitPriceFee.setType(CartItemFeeType.PRICE);
        unitPriceFee.setUnitPrice(cartItem.getUnitPrice());
        unitPriceFee.setUnits(cartItem.getQuantity());

        cartItem.getFees().add(unitPriceFee);
    }
}
