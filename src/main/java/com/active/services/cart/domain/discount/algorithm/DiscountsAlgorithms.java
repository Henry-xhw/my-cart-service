package com.active.services.cart.domain.discount.algorithm;

import com.active.services.DiscountModel;

public class DiscountsAlgorithms {
    private static DiscountAlgorithm BEST = new BestDiscountAlgorithm();
    private static DiscountAlgorithm FLAT_FIRST = new StackableFlatFirstDiscountAlgorithm();

    private DiscountsAlgorithms() {
    }

    public static DiscountAlgorithm bestAlgorithm() {
        return BEST;
    }

    public static DiscountAlgorithm getAlgorithm(DiscountModel model) {
        return model == DiscountModel.COMBINABLE_FLAT_FIRST ? FLAT_FIRST : BEST;
    }
}
