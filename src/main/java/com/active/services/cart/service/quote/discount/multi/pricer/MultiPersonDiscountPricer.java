package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.multi.MultiDiscountUtil;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.AmountType;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;

import java.util.Comparator;
import java.util.List;

import static com.active.services.cart.service.quote.discount.multi.MultiDiscountUtil.uniquePerson;

public class MultiPersonDiscountPricer extends MultiDiscountBasePricer {
    public MultiPersonDiscountPricer(MultiDiscountCartItem mdCartItem,
                                     MultiDiscountThresholdSetting effectiveThresholdSetting) {
        super(mdCartItem, effectiveThresholdSetting);
    }

    @Override
    protected boolean shouldAdvanceTier(CartItem prevItem, CartItem nextItem) {
        return !prevItem.getPersonIdentifier().equals(nextItem.getPersonIdentifier());
    }

    @Override
    protected int countTiers(List<CartItem> effectiveSortedMdCartItems) {
        return uniquePerson(effectiveSortedMdCartItems);
    }

    @Override
    protected Comparator<List<CartItem>> getAllProductComparator() {
        boolean isFlat = AmountType.FLAT == getEffectiveThresholdSetting()
                .getTiers().iterator().next().getAmountType();

        Comparator<List<CartItem>> comparator;

        if (isFlat) {
            comparator = Comparator.comparing(MultiDiscountUtil::quantityCounts).thenComparing(totalPriceThenDbIndex());
        } else {
            comparator = totalPriceThenDbIndex();
        }

        return comparator;
    }
}
