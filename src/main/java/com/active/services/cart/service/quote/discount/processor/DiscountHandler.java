package com.active.services.cart.service.quote.discount.processor;

import com.active.services.cart.service.quote.discount.Discount;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;

import java.util.List;

public interface DiscountHandler {
    List<Discount> loadDiscounts();
    DiscountAlgorithm getDiscountAlgorithm();
}
