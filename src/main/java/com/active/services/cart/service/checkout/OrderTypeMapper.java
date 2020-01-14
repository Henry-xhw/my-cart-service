package com.active.services.cart.service.checkout;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.order.FeeTransactionType;
import com.active.services.order.OrderLineFeeType;

public final class OrderTypeMapper {

    private OrderTypeMapper() {
    }

    static OrderLineFeeType setFeeType(CartItemFeeType type) {
        if (type == CartItemFeeType.PRICE) {
            return OrderLineFeeType.PRICE;
        }
        if (type == CartItemFeeType.PROCESSING_FLAT) {
            return OrderLineFeeType.REGISTRATION_FLAT;
        }
        if (type == CartItemFeeType.PROCESSING_PERCENT) {
            return OrderLineFeeType.REGISTRATION_PERCENT;
        }
        return null;
    }

    static FeeTransactionType setFeeTransactionType(com.active.services.cart.model.FeeTransactionType feeTransactionType) {
        if (feeTransactionType == com.active.services.cart.model.FeeTransactionType.DEBIT) {
            return FeeTransactionType.DEBIT;
        }
        return FeeTransactionType.CREDIT;
    }
}
