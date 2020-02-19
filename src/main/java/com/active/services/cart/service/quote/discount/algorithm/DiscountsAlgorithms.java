package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.CartItem;

import java.util.Currency;

public class DiscountsAlgorithms {

    private DiscountsAlgorithms() {
    }

    public static DiscountAlgorithm bestAlgorithm(CartItem item, Currency currency) {
        return new BestDiscountAlgorithm(item, currency);
    }

    public static DiscountAlgorithm getAlgorithm(CartItem item, DiscountModel model, Currency currency) {
        return model == DiscountModel.COMBINABLE_FLAT_FIRST ? new StackableFlatFirstDiscountAlgorithm() :
                new BestDiscountAlgorithm(item, currency);
    }


}