package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.domain.Discount;

import java.util.List;

public interface DiscountAlgorithm {
    List<Discount> apply(List<Discount> discounts);
}
