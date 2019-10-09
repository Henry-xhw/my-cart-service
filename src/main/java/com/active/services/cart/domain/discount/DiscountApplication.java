package com.active.services.cart.domain.discount;

import com.active.services.cart.domain.cart.CartItem;

import lombok.Data;

import java.util.List;

@Data
public class DiscountApplication {
    private List<CartItem> candidates;
    private List<Discount> discounts;
}
