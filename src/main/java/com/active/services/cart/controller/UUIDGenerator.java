package com.active.services.cart.controller;

import com.active.services.cart.domain.BaseDomainObject;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.v1.BaseDto;
import com.active.services.cart.model.v1.CartItemDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class UUIDGenerator {

    @AfterMapping
    public void generateUUID(@MappingTarget BaseDomainObject baseDomainObject, BaseDto baseDto,
                             @Context boolean isCreate) {
        if (isCreate) {
            baseDomainObject.setIdentifier(UUID.randomUUID());
        }
    }

    @AfterMapping
    public void setGrossAndNetPriceValue(@MappingTarget CartItemDto cartItemDto, CartItem cartItem) {
        Optional.ofNullable(cartItem.getFees()).ifPresent(fees -> fees.stream()
            .filter(cartItemFee -> ObjectUtils.nullSafeEquals(cartItemFee.getType(), CartItemFeeType.PRICE))
            .findFirst().map(cartItemFee -> {cartItemDto.setGrossPrice(cartItemFee.getUnitPrice()
                .multiply(new BigDecimal(cartItemFee.getUnits())));
                // OMS-10128 Net Price = Gross Price - Price Hikes Amount - Discounts Amount
                //Since we didn't plan to implement discount and price hike in cart service at this point, hence the gross price = net price
                cartItemDto.setNetPrice(cartItemDto.getGrossPrice());
                return cartItemFee;
            }));
    }
}
