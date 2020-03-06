package com.active.services.cart.model;

public enum CartItemFeeType {
    PRICE,
    PRICE_ADJUSTMENT,
    PROCESSING_FLAT,
    PROCESSING_PERCENT,
    COUPON_DISCOUNT,
    MULTI_DISCOUNT,
    AA_DISCOUNT,
    MEMBERSHIP_DISCOUNT,
    AD_HOC,
    SURCHARGE;

    public static boolean isPriceDiscount(CartItemFeeType feeType) {
        return feeType == COUPON_DISCOUNT || feeType == MULTI_DISCOUNT ||
                feeType == MEMBERSHIP_DISCOUNT || feeType == AD_HOC;
    }

    public static boolean isActiveProcessingFee(CartItemFeeType feeType) {
        return feeType == PROCESSING_FLAT || feeType == PROCESSING_PERCENT || feeType == AA_DISCOUNT;
    }
}