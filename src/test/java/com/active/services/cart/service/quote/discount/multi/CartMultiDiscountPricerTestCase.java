package com.active.services.cart.service.quote.discount.multi;

import com.active.services.Availability;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountCartItem;
import com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoader;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.discount.multi.DiscountTier;
import com.active.services.product.discount.multi.DiscountTierSortOrder;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountAlgorithm;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;
import com.active.services.product.discount.multi.MultiDiscountType;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.active.services.cart.service.quote.discount.multi.loader.MultiDiscountLoaderTestCase.buildCartItem;
import static com.active.services.cart.service.quote.discount.multi.pricer.MultiDiscountBasePricerTestCase.loadCartQuoteContext;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartMultiDiscountPricerTestCase {

    @Mock
    private MultiDiscountLoader multiDiscountLoader;

    @InjectMocks
    private CartMultiDiscountPricer cartMultiDiscountPricer;

    @Test
    public void quoteSuccess() {

        // four cartItems belong to the same person
        CartItem item1 = buildCartItem();
        item1.setId(1L);
        CartItemFee fee1 = CartDataFactory.cartItemFee(BigDecimal.valueOf(30));
        item1.setQuantity(1);
        item1.setFees(Collections.singletonList(fee1));

        // case 1:LEAST_EXPENSIVE
        MultiDiscount multiDiscount = new MultiDiscount();
        multiDiscount.setDiscountType(MultiDiscountType.MULTI_PERSON);
        multiDiscount.setThreshold(1);
        multiDiscount.setId(1L);
        multiDiscount.setTierSortOrder(DiscountTierSortOrder.ASCENDING_TIER_LEVEL);
        multiDiscount.setAlgorithm(MultiDiscountAlgorithm.LEAST_EXPENSIVE);
        multiDiscount.setAvailability(Availability.ONLINE);
        multiDiscount.setStartDate(new DateTime(LocalDateTime.now().minusDays(1)));
        multiDiscount.setEndDate(new DateTime(LocalDateTime.now().plusDays(1)));
        
        MultiDiscountThresholdSetting multiDiscountThresholdSetting = new MultiDiscountThresholdSetting();
        DiscountTier discountTier = new DiscountTier();
        discountTier.setId(1L);
        discountTier.setAmountType(AmountType.FLAT);
        discountTier.setAmount(BigDecimal.TEN);
        discountTier.setTierLevel(1);
        Set<DiscountTier> discountTiers = new HashSet<>();
        discountTiers.add(discountTier);
        multiDiscountThresholdSetting.setTiers(discountTiers);
        multiDiscountThresholdSetting.setThreshold(1);

        multiDiscount.setThresholdSettings(Collections.singletonList(multiDiscountThresholdSetting));
        MultiDiscountCartItem mdCartItem = new MultiDiscountCartItem(multiDiscount);
        mdCartItem.addCartItems(Collections.singletonList(item1));
        List<MultiDiscountCartItem> mdCartItems = new ArrayList<>();
        mdCartItems.add(mdCartItem);
        when(multiDiscountLoader.load(any())).thenReturn(mdCartItems);

        loadCartQuoteContext();
        cartMultiDiscountPricer.quote(CartQuoteContext.get());
        Assert.assertTrue(item1.getFees().get(0).getSubItems().size() != 0);
        Assert.assertEquals(CartItemFeeType.MULTI_DISCOUNT, item1.getFees().get(0).getSubItems().get(0).getType());
        Assert.assertEquals(10, item1.getFees().get(0).getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(1, item1.getFees().get(0).getSubItems().get(0).getUnits().intValue());
    }
}
