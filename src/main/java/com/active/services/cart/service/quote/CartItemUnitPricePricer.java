package com.active.services.cart.service.quote;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;
import com.active.services.product.nextgen.v1.req.GetProductFeeReq;
import com.active.services.product.nextgen.v1.rsp.GetProductFeeRsp;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import static com.active.services.cart.model.ErrorCode.QUOTE_ERROR;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemUnitPricePricer implements CartItemPricer {

    private final ProductService productService;

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
        if (Objects.isNull(cartItem.getUnitPrice())) {
            GetProductFeeReq getProductFeeReq = new GetProductFeeReq();
            getProductFeeReq.setProductId(cartItem.getProductId());
            getProductFeeReq.setBusinessDate(Instant.now());
            cartItem.getFees().add(CartItemFee.buildCartItemFee(cartItem, getUnitPriceFromProductService(getProductFeeReq),
                    CartItemFeeType.PRICE));
        } else {
            cartItem.getFees().add(CartItemFee.buildCartItemFee(cartItem, CartItemFeeType.PRICE));
        }
        setGrossAndNetPriceValue(cartItem);
    }

    private FeeDto getUnitPriceFromProductService(GetProductFeeReq getProductFeeReq) {
        GetProductFeeRsp result = productService.quote(getProductFeeReq);
        if (BooleanUtils.isFalse(result.isSuccess()) || Objects.isNull(result.getFeeDto())) {
            throw new CartException(QUOTE_ERROR, "Failed to quote for cart: {0}, {1}", result.getErrorCode(),
                    result.getErrorMessage());
        }
        return result.getFeeDto();
    }

    private void setGrossAndNetPriceValue(CartItem cartItem) {
        Optional.ofNullable(cartItem.getFees()).ifPresent(
            fees -> fees.stream().filter(cartItemFee -> Objects.equals(cartItemFee.getType(), CartItemFeeType.PRICE))
                .findAny().map(cartItemFee -> {
                    cartItem.setGrossPrice(cartItemFee.getUnitPrice().multiply(new BigDecimal(cartItemFee.getUnits())));
                    //OMS-10128 Net Price = Gross Price - Price Hikes Amount - Discounts Amount
                    //Since we didn't plan to implement discount and price hike in cart service at this point,
                    //hence the gross price = net price
                    cartItem.setNetPrice(cartItem.getGrossPrice());
                    return cartItemFee;
                }));
    }
}
