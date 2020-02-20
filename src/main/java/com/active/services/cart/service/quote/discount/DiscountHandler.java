package com.active.services.cart.service.quote.discount;

import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;
import com.active.services.cart.service.quote.discount.domain.Discount;

import java.util.List;

public interface DiscountHandler {
    List<Discount> filterDiscounts();
    DiscountAlgorithm getDiscountAlgorithm();
}