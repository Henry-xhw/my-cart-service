package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.domain.CartItem;
import com.active.services.product.Discount;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * Container class to main cart item/coupon relationship.
 *
 */
@Data
@Builder
class CartItemCoupons {
    private CartItem cartItem;

    private List<Discount> couponDiscounts;
}
