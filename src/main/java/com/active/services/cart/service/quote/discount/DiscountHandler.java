package com.active.services.cart.service.quote.discount;

import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;

import java.util.List;

public interface DiscountHandler {

    List<DiscountApplication> filterDiscounts();

    DiscountAlgorithm getDiscountAlgorithm();
}