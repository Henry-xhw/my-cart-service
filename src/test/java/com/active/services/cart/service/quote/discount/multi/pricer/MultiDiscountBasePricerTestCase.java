package com.active.services.cart.service.quote.discount.multi.pricer;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.product.AmountType;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountAlgorithm;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;
import com.active.services.product.discount.multi.MultiDiscountType;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoaderTestCase.buildCartItem;

public class MultiDiscountBasePricerTestCase {
    private MultiDiscountBasePricer multiDiscountBasePricer;
    private List<CartItem> effectiveSortedMdCartItems;

    @Before
    public void setUp() {
        // four cartItems belong to the same person
        CartItem item1 = buildCartItem();
        CartItem item2 = buildCartItem();
        CartItem item3 = buildCartItem();
        CartItem item4 = buildCartItem();

        MultiDiscount multiDiscount = new MultiDiscount();
        multiDiscount.setDiscountType(MultiDiscountType.MULTI_PERSON);
        multiDiscount.setThreshold(2);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.LEAST_EXPENSIVE);

        MultiDiscountCartItem mdCartItem = new MultiDiscountCartItem(multiDiscount);
        mdCartItem.addCartItems(Arrays.asList(item4, item2, item3, item1));

        MultiDiscountThresholdSetting multiDiscountThresholdSetting = new MultiDiscountThresholdSetting();
        DiscountTier discountTier = new DiscountTier();
        discountTier.setAmountType(AmountType.FLAT);
        Set<DiscountTier> discountTiers = new HashSet<>();
        discountTiers.add(discountTier);
        multiDiscountThresholdSetting.setTiers(discountTiers);
        multiDiscountBasePricer = new MultiPersonDiscountPricer(mdCartItem, multiDiscountThresholdSetting);

        effectiveSortedMdCartItems = new ArrayList<>(Arrays.asList(item4, item2, item3, item1));
    }

    @Test
    public void price() {

    }

}
