package com.active.services.cart.service.quote.discount.algorithm;

import com.active.services.DiscountModel;

public class DiscountsAlgorithms {

    private static DiscountAlgorithm best = new BestDiscountAlgorithm();
    private static DiscountAlgorithm flatFirst = new StackableFlatFirstDiscountAlgorithm();

    private DiscountsAlgorithms() {
    }

    public static DiscountAlgorithm bestAlgorithm() {
        return best;
    }

    public static DiscountAlgorithm getAlgorithm(DiscountModel model) {
        return model == DiscountModel.COMBINABLE_FLAT_FIRST ? flatFirst : best;
    }
}