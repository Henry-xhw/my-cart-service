package com.active.services.cart.model;

public enum CouponMode {
    /**
     * The coupon will be used same with {Order#getRequestedCouponCodes()};
     * Generally, The OrderLineDiscountOrigin of corresponding {OrderLineDiscount#getOrigin()} is
     * {com.active.services.order.discount.OrderLineDiscountOrigin#AUTOMATIC}
     * ;
     */
    NORMAL,

    /**
     * The order line level coupon in this mode takes priority over
     * {Order#getRequestedCouponCodes()}; This means even if the coupon is
     * not the best (with a lesser discount amount) one, this coupon will be
     * still applied as long as it satisfies the discount requirement (usage
     * limit, expired date..etc);
     * <p>
     * NOTICE: This mode only makes sense when the discount model is
     * {com.active.services.DiscountModel#NON_COMBINABLE_MINIMIZE_PRICE};
     * <p>
     * Generally, The OrderLineDiscountOrigin of corresponding
     * {OrderLineDiscount#getOrigin()} is
     * {com.active.services.order.discount.OrderLineDiscountOrigin#ORDER_LINE_LEVEL_OVERRIDE}
     * ;
     */
    HIGH_PRIORITY
}
