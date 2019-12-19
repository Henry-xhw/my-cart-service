package com.active.services.cart.service.quote;

import java.math.BigDecimal;
import java.util.Optional;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CartItemUnitPricePricer implements CartItemPricer {
    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
        setGrossAndNetPriceValue(cartItem);
        cartItem.getFees().add(CartItemFee.buildCartItemFee(cartItem, CartItemFeeType.PRICE));
    }

    private void setGrossAndNetPriceValue(CartItem cartItem) {
        Optional.ofNullable(cartItem.getFees()).ifPresent(fees -> fees.stream()
            .filter(cartItemFee -> ObjectUtils.nullSafeEquals(cartItemFee.getType(), CartItemFeeType.PRICE))
            .findFirst().map(cartItemFee -> {cartItem.setGrossPrice(cartItemFee.getUnitPrice()
                .multiply(new BigDecimal(cartItemFee.getUnits())));
                //OMS-10128 Net Price = Gross Price - Price Hikes Amount - Discounts Amount
                //Since we didn't plan to implement discount and price hike in cart service at this point,
                //hence the gross price = net price
                cartItem.setNetPrice(cartItem.getGrossPrice());
                return cartItemFee;
            }));
    }
}
