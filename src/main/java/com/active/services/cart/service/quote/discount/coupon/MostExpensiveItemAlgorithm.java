package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.domain.Cart;

import java.util.List;

/**
 * Iterate all loaded coupon discounts, build the effective cart item coupon.
 *
 * If one coupon has been setup to be MOST_EXPENSIVE and been request to two items,
 * we should only put it to most expensive cart item and removing it from other cart items.
 *
 */
class MostExpensiveItemAlgorithm {

    private List<CartItemCoupons> cartItemCoupons;

    private Cart cart;

    public MostExpensiveItemAlgorithm setCartItemCoupons(List<CartItemCoupons> cartItemCoupons) {
        this.cartItemCoupons = cartItemCoupons;

        return this;
    }

    public MostExpensiveItemAlgorithm setCart(Cart cart) {
        this.cart = cart;

        return this;
    }

    public List<CartItemCoupons> apply() {
        return null;
    }
}
