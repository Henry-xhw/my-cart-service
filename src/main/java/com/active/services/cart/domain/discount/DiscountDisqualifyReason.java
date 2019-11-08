package com.active.services.cart.domain.discount;

public enum DiscountDisqualifyReason {
    /**
     * Indicates that the coupon code in question was not recognized --- it did not match the code on any potential
     * discounts for a given order.
     */
    NOT_RECOGNIZED,
    /**
     * Indicates that the coupon code in question matched the code for one or more discounts but that additional
     * restriction rules attached to one or more discounts where not met.
     */
    RULES_NOT_MET,
    /**
     * Indicates that the coupon code in question has expired - the current date is after the discount's end date.
     */
    EXPIRED,
    /**
     * Indicates the usage limit for the coupon has been reached.
     */
    USAGE_LIMIT_REACHED,
    /**
     * Indicates a better (or equal) discount was found.
     */
    NOT_BEST_DISCOUNT;
}
