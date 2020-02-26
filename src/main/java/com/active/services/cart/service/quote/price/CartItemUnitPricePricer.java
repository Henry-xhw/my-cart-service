package com.active.services.cart.service.quote.price;

import com.active.services.billing.paymentplans.PaymentPlanInstallment;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.service.quote.CartItemFeeBuilder;
import com.active.services.cart.service.quote.CartItemPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemUnitPricePricer implements CartItemPricer {

    private final Map<Long, FeeDto> feeDtoMap;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
        if (Objects.isNull(cartItem.getOverridePrice())) {
            // no override price in CartItem
            cartItem.getFees().add(CartItemFeeBuilder.buildPriceItemFee(cartItem.getQuantity(),
                    feeDtoMap.get(cartItem.getProductId())));
        } else {
            // has override price in CartItem
            cartItem.getFees().add(CartItemFeeBuilder.buildOverridePriceItemFee(cartItem));
        }
        setGrossAndNetPriceValue(cartItem);
    }

    private void setGrossAndNetPriceValue(CartItem cartItem) {
        Optional.ofNullable(cartItem.getFees()).ifPresent(
            fees -> fees.stream().filter(cartItemFee -> Objects.equals(cartItemFee.getType(), CartItemFeeType.PRICE))
                .findAny().ifPresent(cartItemFee -> {
                    cartItem.setGrossPrice(calculateGrossPrice(cartItem, cartItemFee));
                })
        );
    }

    private BigDecimal calculateGrossPrice(CartItem cartItem, @NonNull CartItemFee priceFee) {
        return cartItem.getOverridePrice() != null ? cartItem.getOverridePrice() :
                priceFee.getUnitPrice().multiply(BigDecimal.valueOf(priceFee.getUnits()));
    }

}
