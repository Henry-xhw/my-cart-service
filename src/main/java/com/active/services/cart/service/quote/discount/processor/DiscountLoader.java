package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.service.quote.discount.domain.CartItemDiscounts;

import java.util.List;

public interface DiscountLoader {
    List<CartItemDiscounts> load();
}
