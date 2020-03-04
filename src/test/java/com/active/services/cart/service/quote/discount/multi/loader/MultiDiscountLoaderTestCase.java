package com.active.services.cart.service.quote.discount.multi.loader;

import com.active.platform.concurrent.TaskRunner;
import com.active.services.cart.common.CartServiceConfigurator;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.domain.DateTime;
import com.active.services.product.api.omsOnly.soap.ProductOMSEndpoint;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountThresholdSetting;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.unitils.reflectionassert.ReflectionAssert;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class MultiDiscountLoaderTestCase {

    private static Random productIdRandom = new Random();

    @Spy
    private TaskRunner taskRunner = new CartServiceConfigurator().taskRunner();

    @Mock
    private ProductOMSEndpoint productOMSEndpoint;

    @InjectMocks
    private MultiDiscountLoader multiDiscountLoader;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void load()  {
        // Should ignore
        CartItem ignoredCartItem = buildCartItem();
        ignoredCartItem.setIgnoreMultiDiscounts(true);

        CartItem cartItem1Product1 = buildCartItem();
        CartItem cartItem2Product1 = buildCartItem();
        // The same product/biz date
        cartItem2Product1.setBusinessDate(cartItem1Product1.getBusinessDate());
        cartItem2Product1.setProductId(cartItem1Product1.getProductId());

        CartItem cartItem3 = buildCartItem();
        CartItem cartItem4 = buildCartItem();

        MultiDiscount md1 = getMultiDiscount();
        MultiDiscount md2 = getMultiDiscount();
        MultiDiscount md3 = getMultiDiscount();
        MultiDiscount md4 = getMultiDiscount();

        Cart cart = new Cart();
        cart.setItems(Arrays.asList(ignoredCartItem, cartItem1Product1, cartItem2Product1, cartItem3, cartItem4));

        Long productId1 = cartItem1Product1.getProductId();
        DateTime bd1 = new DateTime(Date.from(cartItem1Product1.getBusinessDate()));
        when(productOMSEndpoint.findEffectiveMultiDiscountsWithDate(isNull(), eq(productId1), eq(bd1)))
                .thenReturn(Arrays.asList(md1, md2, md3));

        Long productId3 = cartItem3.getProductId();
        DateTime bd3 = new DateTime(Date.from(cartItem3.getBusinessDate()));
        when(productOMSEndpoint.findEffectiveMultiDiscountsWithDate(isNull(), eq(productId3), eq(bd3)))
                .thenReturn(Arrays.asList(md2, md3, md4));

        Long productId4 = cartItem4.getProductId();
        DateTime bd4 = new DateTime(Date.from(cartItem4.getBusinessDate()));
        when(productOMSEndpoint.findEffectiveMultiDiscountsWithDate(isNull(), eq(productId4), eq(bd4)))
                .thenReturn(Arrays.asList(md4));

        List<MultiDiscountCartItem> expectedResults = new ArrayList<>();
        MultiDiscountCartItem r1 = new MultiDiscountCartItem(md1);
        r1.addCartItems(Arrays.asList(cartItem1Product1, cartItem2Product1));
        expectedResults.add(r1);

        MultiDiscountCartItem r2 = new MultiDiscountCartItem(md2);
        r2.addCartItems(Arrays.asList(cartItem1Product1, cartItem2Product1, cartItem3));
        expectedResults.add(r2);

        MultiDiscountCartItem r3 = new MultiDiscountCartItem(md3);
        r3.addCartItems(Arrays.asList(cartItem1Product1, cartItem2Product1, cartItem3));
        expectedResults.add(r3);

        MultiDiscountCartItem r4 = new MultiDiscountCartItem(md4);
        r4.addCartItems(Arrays.asList(cartItem3, cartItem4));
        expectedResults.add(r4);

        List<MultiDiscountCartItem> results = multiDiscountLoader.load(cart);

        ReflectionAssert.assertLenientEquals(expectedResults, results);
    }

    public MultiDiscount getMultiDiscount() {
        MultiDiscount md = new MultiDiscount();
        md.setId(productIdRandom.nextLong());
        md.setThresholdSettings(Arrays.asList(new MultiDiscountThresholdSetting(10, new HashSet<>())));

        return md;
    }

    public static CartItem buildCartItem() {
        CartItem cartItem = spy(new CartItem());
        cartItem.setIdentifier(UUID.randomUUID());
        cartItem.setIgnoreMultiDiscounts(false);
        cartItem.setQuantity(1);
        cartItem.setPersonIdentifier(UUID.randomUUID().toString());
        cartItem.setBusinessDate(Instant.now());
        cartItem.setProductId(productIdRandom.nextLong());
        doReturn(true).when(cartItem).isNetPriceNotZero();

        return cartItem;
    }
}
