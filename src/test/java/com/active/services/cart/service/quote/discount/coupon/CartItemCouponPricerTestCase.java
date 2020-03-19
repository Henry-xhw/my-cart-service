package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.DiscountFactory;
import com.active.services.cart.service.quote.discount.DiscountMapper;
import com.active.services.domain.DateTime;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.Product;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.active.services.cart.service.quote.discount.multi.pricer.MultiDiscountBasePricerTestCase.loadCartQuoteContext;

public class CartItemCouponPricerTestCase {

    private CartItemCouponPricer cartItemCouponPricer;
    private CouponDiscountContext couponDiscountContext;
    private List<Discount> itemDiscounts;
    private CartItem item;
    private CartQuoteContext cartQuoteContext;

    @Before
    public void setup() {
        itemDiscounts = new ArrayList<>();
        couponDiscountContext = new CouponDiscountContext();
        item = CartDataFactory.getCartItem(1, new BigDecimal(100), "description");
        loadCartQuoteContext();
        cartQuoteContext = CartQuoteContext.get();
    }

    /**
     * price = 100
     * cart item level discount1   FLAT    10
     * cart item level discount2   PERCENT 20%
     * <p>
     * case 1: all coupon discounts are expired
     * expected: no discount will be applied
     */

    @Test
    public void doQuoteWhenNoSatisfiedDiscountExpired() {

        Discount desc1 = DiscountFactory.getDiscount(AmountType.FLAT, new BigDecimal("10.00"), "code1",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().minusDays(1)));
        desc1.setUsageLimit(20);
        Discount desc2 = DiscountFactory.getDiscount(AmountType.PERCENT, new BigDecimal("20.00"), "code2",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().minusDays(1)));
        desc2.setUsageLimit(15);
        itemDiscounts.add(desc1);
        itemDiscounts.add(desc2);
        cartItemCouponPricer = new CartItemCouponPricer(couponDiscountContext, itemDiscounts);

        cartItemCouponPricer.doQuote(CartQuoteContext.get(), item);
        Optional<CartItemFee> priceCartItemFee = item.getPriceCartItemFee();
        Assert.assertEquals(0, priceCartItemFee.get().getSubItems().size());
    }

    /**
     * price = 100
     * cart item level discount1   FLAT    10
     * cart item level discount2   PERCENT 20%
     * <p>
     * case 2: all coupon discounts are used out
     * expected: no discount will be applied
     */
    @Test
    public void doQuoteWhenNoSatisfiedDiscountOutUsage() {
        Discount desc1 = DiscountFactory.getDiscount(AmountType.FLAT, new BigDecimal("10.00"), "code1",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc1.setUsageLimit(20);
        Discount desc2 = DiscountFactory.getDiscount(AmountType.PERCENT, new BigDecimal("20.00"), "code2",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc2.setUsageLimit(15);
        itemDiscounts.add(desc1);
        itemDiscounts.add(desc2);

        List<DiscountUsage> discountUsages = buildDiscountUsages(desc1, desc2, true);
        couponDiscountContext.setDiscountUsages(discountUsages);
        Map<CartItem, List<Discount>> cartItemDiscountMap = new HashMap<>();
        cartItemDiscountMap.put(item, itemDiscounts);
        couponDiscountContext.setCartItemDiscountMap(cartItemDiscountMap);
        cartItemCouponPricer = new CartItemCouponPricer(couponDiscountContext, itemDiscounts);
        cartItemCouponPricer.doQuote(CartQuoteContext.get(), item);
        Optional<CartItemFee> priceCartItemFee = item.getPriceCartItemFee();
        Assert.assertEquals(0, priceCartItemFee.get().getSubItems().size());
    }

    /**
     * price = 100
     * cart item level discount1   FLAT    10
     * cart item level discount2   PERCENT 20%
     * <p>
     * case 3: discount1 discount2 are not expired and not used out, but discount1 was applied already,
     * expected: only discount2 will be applied
     */
    @Test
    public void doQuoteWhenUniqueSpecDiscount() {
        Discount desc1 = DiscountFactory.getDiscount(AmountType.FLAT, new BigDecimal("10.00"), "code1",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc1.setUsageLimit(20);
        Discount desc2 = DiscountFactory.getDiscount(AmountType.PERCENT, new BigDecimal("20.00"), "code2",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc2.setUsageLimit(15);
        itemDiscounts.add(desc1);
        itemDiscounts.add(desc2);

        cartQuoteContext.addAppliedDiscount(DiscountMapper.MAPPER.toDiscount(desc1, cartQuoteContext));

        List<DiscountUsage> discountUsages = buildDiscountUsages(desc1, desc2, false);
        couponDiscountContext.setDiscountUsages(discountUsages);
        Map<CartItem, List<Discount>> cartItemDiscountMap = new HashMap<>();
        cartItemDiscountMap.put(item, itemDiscounts);
        couponDiscountContext.setCartItemDiscountMap(cartItemDiscountMap);
        cartItemCouponPricer = new CartItemCouponPricer(couponDiscountContext, itemDiscounts);
        cartItemCouponPricer.doQuote(CartQuoteContext.get(), item);
        Optional<CartItemFee> priceCartItemFee = item.getPriceCartItemFee();
        Assert.assertEquals(1, priceCartItemFee.get().getSubItems().size());
        Assert.assertTrue(priceCartItemFee.get().getSubItems().stream().allMatch(cartItemFee -> cartItemFee.getType() == CartItemFeeType.COUPON_DISCOUNT));
        Assert.assertEquals(20, priceCartItemFee.get().getSubItems().get(0).getUnitPrice().intValue());
    }

    /**
     * price = 100
     * cart item level discount1   FLAT    10
     * cart item level discount2   PERCENT 20%
     * <p>
     * case 4:
     * DiscountModel: combinable
     * Algorithm:     StackableFlatFirstDiscountAlgorithm
     * expected: discount1 and discount2 will be both applied
     */

    @Test
    public void doQuoteWhenCombinableModeAndStackAlgorithm() {
        Discount desc1 = DiscountFactory.getDiscount(AmountType.FLAT, new BigDecimal("10.00"), "code1",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc1.setUsageLimit(20);
        Discount desc2 = DiscountFactory.getDiscount(AmountType.PERCENT, new BigDecimal("20.00"), "code2",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc2.setUsageLimit(15);
        itemDiscounts.add(desc1);
        itemDiscounts.add(desc2);

        List<DiscountUsage> discountUsages = buildDiscountUsages(desc1, desc2, false);
        couponDiscountContext.setDiscountUsages(discountUsages);
        Map<CartItem, List<Discount>> cartItemDiscountMap = new HashMap<>();
        cartItemDiscountMap.put(item, itemDiscounts);
        couponDiscountContext.setCartItemDiscountMap(cartItemDiscountMap);
        cartItemCouponPricer = new CartItemCouponPricer(couponDiscountContext, itemDiscounts);
        cartItemCouponPricer.doQuote(CartQuoteContext.get(), item);
        Optional<CartItemFee> priceCartItemFee = item.getPriceCartItemFee();
        Assert.assertEquals(2, priceCartItemFee.get().getSubItems().size());
        Assert.assertTrue(priceCartItemFee.get().getSubItems().stream().allMatch(cartItemFee -> cartItemFee.getType() == CartItemFeeType.COUPON_DISCOUNT));
        Assert.assertEquals(10, priceCartItemFee.get().getSubItems().get(0).getUnitPrice().intValue());
        Assert.assertEquals(18.0, priceCartItemFee.get().getSubItems().get(1).getUnitPrice().floatValue(), 2);
    }

    /**
     * price = 100
     * cart item level discount1   FLAT    10
     * cart item level discount2   PERCENT 20%
     * cart level      discount3   FLAT    15
     * <p>
     * case 5:
     * DiscountModel:none combinable
     * Algorithm:    BestDiscountAlgorithm
     * Priority:     Normal
     * expected: only discount2 will be applied
     */

    @Test
    public void doQuoteWhenNoCombinableModeAndBestAlgorithmNormalPriority() {
        Discount desc1 = DiscountFactory.getDiscount(AmountType.FLAT, new BigDecimal("10.00"), "code1",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc1.setUsageLimit(20);
        Discount desc2 = DiscountFactory.getDiscount(AmountType.PERCENT, new BigDecimal("20.00"), "code2",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        Discount desc3 = DiscountFactory.getDiscount(AmountType.FIXED_AMOUNT, new BigDecimal("85.00"), "code3",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc2.setUsageLimit(15);
        itemDiscounts.add(desc1);
        itemDiscounts.add(desc2);
        itemDiscounts.add(desc3);

        List<DiscountUsage> discountUsages = buildDiscountUsages(desc1, desc2, false);
        couponDiscountContext.setDiscountUsages(discountUsages);
        Map<CartItem, List<Discount>> cartItemDiscountMap = new HashMap<>();
        cartItemDiscountMap.put(item, itemDiscounts);
        couponDiscountContext.setCartItemDiscountMap(cartItemDiscountMap);
        cartItemCouponPricer = new CartItemCouponPricer(couponDiscountContext, itemDiscounts);
        CartQuoteContext cartQuoteContext = CartQuoteContext.get();
        Product product = getProduct(item.getProductId(), DiscountModel.NON_COMBINABLE_MINIMIZE_PRICE);
        cartQuoteContext.setProducts(Collections.singletonList(product));
        item.setCouponMode(CouponMode.NORMAL);
        HashSet<String> couponCodes = new HashSet<>();
        couponCodes.add(desc1.getCouponCode());
        item.setCouponCodes(couponCodes);
        cartItemCouponPricer.doQuote(cartQuoteContext, item);
        Optional<CartItemFee> priceCartItemFee = item.getPriceCartItemFee();
        Assert.assertEquals(1, priceCartItemFee.get().getSubItems().size());
        Assert.assertTrue(priceCartItemFee.get().getSubItems().stream().allMatch(cartItemFee -> cartItemFee.getType() == CartItemFeeType.COUPON_DISCOUNT));
        Assert.assertEquals(20, priceCartItemFee.get().getSubItems().get(0).getUnitPrice().intValue());
    }

    /**
     * price = 100
     * cart item level discount1   FLAT    10
     * cart item level discount2   PERCENT 20%
     * cart level      discount3   FLAT    15
     * <p>
     * <p>
     * case 6:
     * DiscountModel:none combinable
     * Algorithm:    BestDiscountAlgorithm
     * Priority:     High priority
     * expected: only cart item level discount and the best discount will be applied, which is discount2
     */

    @Test
    public void doQuoteWhenNoCombinableModeAndBestAlgorithmHighPriority() {
        Discount desc1 = DiscountFactory.getDiscount(AmountType.FLAT, new BigDecimal("10.00"), "code1",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc1.setUsageLimit(20);
        Discount desc2 = DiscountFactory.getDiscount(AmountType.PERCENT, new BigDecimal("20.00"), "code2",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        Discount desc3 = DiscountFactory.getDiscount(AmountType.FIXED_AMOUNT, new BigDecimal("85.00"), "code3",
                DiscountAlgorithm.MOST_EXPENSIVE, new DateTime(LocalDateTime.now().minusDays(1)),
                new DateTime(LocalDateTime.now().plusDays(1)));
        desc2.setUsageLimit(15);
        itemDiscounts.add(desc1);
        itemDiscounts.add(desc2);
        itemDiscounts.add(desc3);

        List<DiscountUsage> discountUsages = buildDiscountUsages(desc1, desc2, false);
        couponDiscountContext.setDiscountUsages(discountUsages);
        Map<CartItem, List<Discount>> cartItemDiscountMap = new HashMap<>();
        cartItemDiscountMap.put(item, itemDiscounts);
        couponDiscountContext.setCartItemDiscountMap(cartItemDiscountMap);
        cartItemCouponPricer = new CartItemCouponPricer(couponDiscountContext, itemDiscounts);
        CartQuoteContext cartQuoteContext = CartQuoteContext.get();
        Product product = getProduct(item.getProductId(), DiscountModel.NON_COMBINABLE_MINIMIZE_PRICE);
        cartQuoteContext.setProducts(Collections.singletonList(product));
        item.setCouponMode(CouponMode.HIGH_PRIORITY);
        HashSet<String> couponCodes = new HashSet<>();
        couponCodes.add(desc1.getCouponCode());
        couponCodes.add(desc2.getCouponCode());
        item.setCouponCodes(couponCodes);
        cartItemCouponPricer.doQuote(cartQuoteContext, item);
        Optional<CartItemFee> priceCartItemFee = item.getPriceCartItemFee();
        Assert.assertEquals(1, priceCartItemFee.get().getSubItems().size());
        Assert.assertTrue(priceCartItemFee.get().getSubItems().stream().allMatch(cartItemFee -> cartItemFee.getType() == CartItemFeeType.COUPON_DISCOUNT));
        Assert.assertEquals(20, priceCartItemFee.get().getSubItems().get(0).getUnitPrice().intValue());
    }


    private List<DiscountUsage> buildDiscountUsages(Discount desc1, Discount desc2, boolean isUsedOut) {
        List<DiscountUsage> discountUsages = new ArrayList<>();
        int usage1 = 0;
        int usage2 = 0;
        if (isUsedOut) {
            usage1 = desc1.getUsageLimit();
            usage2 = desc2.getUsageLimit();
        }
        DiscountUsage discountUsage1 = DiscountFactory.getDiscountUsage(desc1.getId(), usage1, desc1.getUsageLimit());
        DiscountUsage discountUsage2 = DiscountFactory.getDiscountUsage(desc2.getId(), usage2, desc2.getUsageLimit());
        discountUsages.add(discountUsage1);
        discountUsages.add(discountUsage2);
        return discountUsages;
    }

    private Product getProduct(Long prodId, DiscountModel model) {
        Product product = new Product();
        product.setId(prodId);
        product.setDiscountModel(model);
        return product;
    }
}
