package com.active.services.cart.model;

public enum DiscountOrigin {
    /**
     * the discount was manually added by the event owner
     */
    AD_HOC,

    /**
     * the discount was automatically added when the order was priced
     */
    AUTOMATIC,

    /**
     * Related to Override, it's automatically overridden by the system
     */
    AUTOMATIC_OVERRIDE,

    /**
     * the discount was automatically added when transfer or create subsequent orders
     */
    CARRY_OVER,

    /**
     * used when multiple discounts are qualified but only the best discount will be applied
     */
    ORDER_LINE_LEVEL_OVERRIDE,

    /**
     * used when disqualified discount will be manually applied to an order line
     */
    OVERRIDE;

    public boolean isAutoRemovedCouponDiscount() {
        switch(this) {
            case AUTOMATIC:
            case ORDER_LINE_LEVEL_OVERRIDE:
            case CARRY_OVER:
                return true;
            default:
                return false;
        }
    }
}
