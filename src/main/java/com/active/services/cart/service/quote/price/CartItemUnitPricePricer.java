package com.active.services.cart.service.quote.price;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
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

    private final Map<Long, FeeDto> feeDtoMap;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
        CartItemFee priceFee;

        if (Objects.isNull(cartItem.getUnitPrice())) {
            priceFee = CartItemFee.buildCartItemFee(cartItem,
                    feeDtoMap.get(cartItem.getProductId()), CartItemFeeType.PRICE);
        } else {
            priceFee = CartItemFee.buildCartItemFee(cartItem, CartItemFeeType.PRICE);
        }
        priceFee.setDueAmount(priceFee.getUnitPrice());
        cartItem.getFees().add(priceFee);
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
