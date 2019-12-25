package com.active.services.cart.infrastructure.mapper;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.order.FeeTransactionType;
import com.active.services.order.OrderLineFeeType;

public class OrderTypeMapping {

    OrderLineFeeType setFeeType(CartItemFeeType type) {
        if (type == CartItemFeeType.PRICE) {
            return OrderLineFeeType.PRICE;
        }
        if (type == CartItemFeeType.PRICE_ADJUSTMENT) {
            return OrderLineFeeType.ADJUSTMENT_CREDIT;
        }
        if (type == CartItemFeeType.PROCESSING_FLAT) {
            return OrderLineFeeType.REGISTRATION_FLAT;
        }
        if (type == CartItemFeeType.PROCESSING_PERCENT) {
            return OrderLineFeeType.REGISTRATION_PERCENT;
        }
        return null;
    }

    FeeTransactionType setFeeTransactionType(com.active.services.cart.model.FeeTransactionType feeTransactionType) {
        if (feeTransactionType == com.active.services.cart.model.FeeTransactionType.DEBIT) {
            return FeeTransactionType.DEBIT;
        }
        return FeeTransactionType.CREDIT;
    }
}
