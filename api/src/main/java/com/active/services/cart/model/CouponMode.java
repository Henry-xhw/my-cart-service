package com.active.services.cart.model;

public enum CouponMode {
    /**
     *
     * The coupon in this mode takes normal priority.
     */
    NORMAL,

    /**
     * The coupon in this mode takes priority over
     * This means even if the coupon is not the best (with a lesser discount amount) one,
     * this coupon will be still applied as long as it satisfies the discount requirement
     * (usage limit, expired date..etc);
     */
    HIGH_PRIORITY
}
