package com.active.services.cart.model;

public enum AmountType {
    PERCENT,
    FLAT,
    FIXED_AMOUNT;
    private AmountType() {
    }
    public static boolean isFlatOrPercent(AmountType type) {
        return type == PERCENT || type == FLAT;
    }
}
