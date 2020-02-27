package com.active.services.cart.service.checkout;

import com.active.services.cart.model.CartItemFeeType;
import com.active.services.order.FeeTransactionType;
import com.active.services.order.OrderLineFeeType;

public final class OrderTypeMapper {

    private OrderTypeMapper() {
    }

    public static OrderLineFeeType setFeeType(CartItemFeeType type) {
        switch (type) {
            case PRICE:
                return OrderLineFeeType.PRICE;
            case PROCESSING_FLAT:
                return OrderLineFeeType.REGISTRATION_FLAT;
            case PROCESSING_PERCENT:
                return OrderLineFeeType.REGISTRATION_PERCENT;
            case COUPON_DISCOUNT:
                return OrderLineFeeType.COUPON_DISCOUNT;
            case MULTI_DISCOUNT:
                return OrderLineFeeType.MULTI_DISCOUNT;
            case AA_DISCOUNT:
                return OrderLineFeeType.AA_DISCOUNT;
            case MEMBERSHIP_DISCOUNT:
                return OrderLineFeeType.MEMBERSHIP_DISCOUNT;
            case AD_HOC:
                return OrderLineFeeType.AD_HOC;
            default:
                return OrderLineFeeType.AD_HOC;
        }
    }

    public static FeeTransactionType setFeeTransactionType(com.active.services.cart.model.FeeTransactionType feeTransactionType) {
        if (feeTransactionType == com.active.services.cart.model.FeeTransactionType.DEBIT) {
            return FeeTransactionType.DEBIT;
        }
        return FeeTransactionType.CREDIT;
    }
}
