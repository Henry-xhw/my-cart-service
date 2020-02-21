package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.cart.service.quote.discount.DiscountApplication;

import java.util.List;

public interface DiscountAlgorithm {
    List<DiscountApplication> apply(List<DiscountApplication> discounts);
}
