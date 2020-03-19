package com.active.services.cart.service.quote.discount.coupon;

import com.active.platform.concurrent.ExecutorServiceTaskRunner;
import com.active.platform.concurrent.TaskRunner;
import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.Discount;
import com.active.services.product.api.omsOnly.soap.ProductOMSEndpoint;

import io.micrometer.core.instrument.util.NamedThreadFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class CouponDiscountLoaderTestCase extends BaseTestCase {

    @Mock
    private SOAPClient soapClient;

    private TaskRunner taskRunner;

    @Mock
    private ProductOMSEndpoint productOMSEndpoint;

    @Before
    public void setUp() {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        when(soapClient.getProductOMSEndpoint()).thenReturn(productOMSEndpoint);
        taskRunner = new ExecutorServiceTaskRunner(new ThreadPoolExecutor(5, 100,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory("cart-backend-compute-thread-")));
    }

    @Test
    public void loadSuccess() {
        when(productOMSEndpoint.findLatestDiscountsByProductIdAndCouponCodes(any(), any(), any()))
                .thenReturn(Collections.singletonList(new Discount()));
        CartQuoteContext context = new CartQuoteContext(CartDataFactory.cart());
        List<CartItem> noneZeroItems = context.getCart().getFlattenCartItems().stream()
                .filter(CartItem::isNetPriceNotZero).collect(toList());
        CouponDiscountLoader loader = new CouponDiscountLoader(context, noneZeroItems);
        ReflectionTestUtils.setField(loader, "soapClient", soapClient);
        ReflectionTestUtils.setField(loader, "taskRunner", taskRunner);
        Map<CartItem, List<Discount>> discounts = loader.loadDiscounts();
        assertEquals(2, discounts.size());
    }

    @Test
    public void loadWhenRequestedCodesIsEmpty() {
        Cart cart = CartDataFactory.cart();
        cart.setCouponCodes(new HashSet<>());
        cart.getItems().forEach(item -> item.setCouponCodes(new HashSet<>()));
        CartQuoteContext context = new CartQuoteContext(cart);
        List<CartItem> noneZeroItems = context.getCart().getFlattenCartItems().stream()
                .filter(CartItem::isNetPriceNotZero).collect(toList());
        CouponDiscountLoader loader = new CouponDiscountLoader(context, noneZeroItems);
        Map<CartItem, List<Discount>> discounts = loader.loadDiscounts();
        assertEquals(0, discounts.size());
    }

    @Test
    public void loadWhenFindLatestDiscountsIsEmpty() {
        when(productOMSEndpoint.findLatestDiscountsByProductIdAndCouponCodes(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        CartQuoteContext context = new CartQuoteContext(CartDataFactory.cart());
        List<CartItem> noneZeroItems = context.getCart().getFlattenCartItems().stream()
                .filter(CartItem::isNetPriceNotZero).collect(toList());
        CouponDiscountLoader loader = new CouponDiscountLoader(context, noneZeroItems);
        ReflectionTestUtils.setField(loader, "soapClient", soapClient);
        ReflectionTestUtils.setField(loader, "taskRunner", taskRunner);
        Map<CartItem, List<Discount>> discounts = loader.loadDiscounts();
        assertEquals(0, discounts.size());
    }
}