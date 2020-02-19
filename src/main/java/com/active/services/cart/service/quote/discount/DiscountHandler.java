package com.active.services.cart.service.quote.discount;

import java.util.List;

public interface DiscountHandler {
    List<Discount> loadDiscounts();
    List<Discount> filterAndSort(List<Discount> discounts);
}
