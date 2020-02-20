package com.active.services.cart.controller.v1.mapper;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.service.checkout.OrderTypeMapper;
import com.active.services.order.FeeTransactionType;
import com.active.services.order.OrderLineFeeType;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTypeMapperTestCase {

    @Test
    public void setFeeType() {
        assertEquals(OrderLineFeeType.PRICE, OrderTypeMapper.setFeeType(CartItemFeeType.PRICE));
        assertEquals(OrderLineFeeType.REGISTRATION_PERCENT, OrderTypeMapper.setFeeType(CartItemFeeType.PROCESSING_PERCENT));
        assertEquals(OrderLineFeeType.REGISTRATION_FLAT, OrderTypeMapper.setFeeType(CartItemFeeType.PROCESSING_FLAT));
    }

    @Test
    public void setFeeTransactionType() {
        assertEquals(FeeTransactionType.DEBIT,
                OrderTypeMapper.setFeeTransactionType(com.active.services.cart.model.FeeTransactionType.DEBIT));
        assertEquals(FeeTransactionType.CREDIT,
                OrderTypeMapper.setFeeTransactionType(com.active.services.cart.model.FeeTransactionType.CREDIT));
    }
}
