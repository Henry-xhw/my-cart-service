package com.active.services.cart.infrastructure.mapper;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.order.FeeTransactionType;
import com.active.services.order.OrderLineFeeType;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderTypeMappingTestCase {

    @Test
    public void setFeeType() {
        assertEquals(OrderLineFeeType.PRICE, OrderTypeMapping.setFeeType(CartItemFeeType.PRICE));
        assertEquals(OrderLineFeeType.REGISTRATION_PERCENT, OrderTypeMapping.setFeeType(CartItemFeeType.PROCESSING_PERCENT));
        assertEquals(OrderLineFeeType.REGISTRATION_FLAT, OrderTypeMapping.setFeeType(CartItemFeeType.PROCESSING_FLAT));
        assertEquals(OrderLineFeeType.ADJUSTMENT_CREDIT, OrderTypeMapping.setFeeType(CartItemFeeType.PRICE_ADJUSTMENT));
    }

    @Test
    public void setFeeTransactionType() {
        assertEquals(FeeTransactionType.DEBIT,
                OrderTypeMapping.setFeeTransactionType(com.active.services.cart.model.FeeTransactionType.DEBIT));
        assertEquals(FeeTransactionType.CREDIT,
                OrderTypeMapping.setFeeTransactionType(com.active.services.cart.model.FeeTransactionType.CREDIT));
    }
}
