package com.active.services.cart.service.quote.price;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemUnitPricePricer implements CartItemPricer {

    private final Map<Long, FeeDto> feeDtoHashMap;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
        if (Objects.isNull(cartItem.getUnitPrice())) {
            cartItem.getFees().add(CartItemFeeBuilder.buildPriceItemFee(cartItem, feeDtoHashMap.get(cartItem.getProductId())));
        } else {
            cartItem.getFees().add(CartItemFeeBuilder.buildOverridePriceItemFee(cartItem));
        }
        setGrossAndNetPriceValue(cartItem);
    }

    private void setGrossAndNetPriceValue(CartItem cartItem) {
        Optional.ofNullable(cartItem.getFees()).ifPresent(
            fees -> fees.stream().filter(cartItemFee -> Objects.equals(cartItemFee.getType(), CartItemFeeType.PRICE))
                .findAny().ifPresent(cartItemFee -> {
                    cartItem.setGrossPrice(cartItemFee.getUnitPrice().multiply(new BigDecimal(cartItemFee.getUnits())));
                    //OMS-10128 Net Price = Gross Price - Price Hikes Amount - Discounts Amount
                    //Since we didn't plan to implement cartDiscount and price hike in cart service at this point,
                    //hence the gross price = net price
                    cartItem.setNetPrice(cartItem.getGrossPrice());
                })
        );
    }
}
