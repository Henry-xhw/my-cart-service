package com.active.services.cart.service.quote.discount;

import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;

import java.util.List;

public interface DiscountHandler {

    List<Discount> filterDiscounts();

    DiscountAlgorithm getDiscountAlgorithm();
}