package com.active.services.cart.service.checkout;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.order.FeeTransactionType;
import com.active.services.order.OrderLineFeeType;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTypeMapperTestCase {

    @Test
    public void setFeeTypeSuccess() {
        assertEquals(OrderLineFeeType.PRICE, OrderTypeMapper.setFeeType(CartItemFeeType.PRICE));
        assertEquals(OrderLineFeeType.REGISTRATION_FLAT, OrderTypeMapper.setFeeType(CartItemFeeType.PROCESSING_FLAT));
        assertEquals(OrderLineFeeType.REGISTRATION_PERCENT,
                OrderTypeMapper.setFeeType(CartItemFeeType.PROCESSING_PERCENT));
        assertEquals(OrderLineFeeType.COUPON_DISCOUNT, OrderTypeMapper.setFeeType(CartItemFeeType.COUPON_DISCOUNT));
        assertEquals(OrderLineFeeType.MULTI_DISCOUNT, OrderTypeMapper.setFeeType(CartItemFeeType.MULTI_DISCOUNT));
        assertEquals(OrderLineFeeType.AA_DISCOUNT, OrderTypeMapper.setFeeType(CartItemFeeType.AA_DISCOUNT));
        assertEquals(OrderLineFeeType.MEMBERSHIP_DISCOUNT,
                OrderTypeMapper.setFeeType(CartItemFeeType.MEMBERSHIP_DISCOUNT));
        assertEquals(OrderLineFeeType.AD_HOC, OrderTypeMapper.setFeeType(CartItemFeeType.AD_HOC));
    }

    @Test
    public void setFeeTransactionTypeSuccess() {
        assertEquals(FeeTransactionType.DEBIT, OrderTypeMapper.setFeeTransactionType(
                com.active.services.cart.model.FeeTransactionType.DEBIT));
        assertEquals(FeeTransactionType.CREDIT, OrderTypeMapper.setFeeTransactionType(
                com.active.services.cart.model.FeeTransactionType.CREDIT));
    }
}
