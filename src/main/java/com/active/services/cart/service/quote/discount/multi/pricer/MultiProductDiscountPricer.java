package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;

import java.util.Comparator;
import java.util.List;

import static com.active.services.cart.service.quote.discount.multi.MultiDiscountUtil.quantityCounts;

public class MultiProductDiscountPricer extends MultiDiscountBasePricer {
    public MultiProductDiscountPricer(MultiDiscountCartItem mdCartItem,
                                      MultiDiscountThresholdSetting effectiveThresholdSetting) {
        super(mdCartItem, effectiveThresholdSetting);
    }

    @Override
    protected boolean shouldAdvanceTier(CartItem prevItem, CartItem nextItem) {
        return true;
    }

    @Override
    protected int countTiers(List<CartItem> effectiveSortedMdCartItems) {
        return quantityCounts(effectiveSortedMdCartItems);
    }

    @Override
    protected Comparator<List<CartItem>> getAllProductComparator() {
        return totalPriceThenDbIndex();
    }
}
