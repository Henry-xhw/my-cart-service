package com.active.services.cart.service.quote.discount.coupon;

import com.active.platform.concurrent.ExecutorServiceTaskRunner;
import com.active.platform.concurrent.TaskRunner;
import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.api.omsOnly.soap.ProductOMSEndpoint;

import io.micrometer.core.instrument.util.NamedThreadFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;

public class CouponDiscountLoaderTestCase extends BaseTestCase {

    private SOAPClient soapClient;

    private TaskRunner taskRunner;

    private ProductOMSEndpoint productOMSEndpoint;

    @Before
    public void setUp() {
        super.setUp();
        soapClient = Mockito.mock(SOAPClient.class);
        taskRunner = new ExecutorServiceTaskRunner(new ThreadPoolExecutor(5, 100,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory("cart-backend-compute-thread-")));
        productOMSEndpoint = Mockito.mock(ProductOMSEndpoint.class);
        Mockito.when(soapClient.getProductOMSEndpoint()).thenReturn(productOMSEndpoint);
    }

    @Test
    public void loadSuccess() {
        Cart cart = CartDataFactory.cart();
        CartItem cartItem1 = CartDataFactory.cartItem().setNetPrice(BigDecimal.valueOf(1L));
        CartItem cartItem2 = CartDataFactory.cartItem().setNetPrice(BigDecimal.valueOf(2L));
        cart.setItems(Arrays.asList(cartItem1, cartItem2));
        CartQuoteContext context = new CartQuoteContext(cart);
        Mockito.when(productOMSEndpoint.findLatestDiscountsByProductIdAndCouponCodes(any(), any(), any()))
                .thenReturn(Collections.singletonList(buildDiscount()));
        CouponDiscountLoader loader = CouponDiscountLoader.builder().context(context).soapClient(soapClient)
                .taskRunner(taskRunner).build();

        List<CartItemDiscounts> result = loader.load();
        Assert.assertEquals(cartItem2, result.get(0).getCartItem());
        Assert.assertEquals(cartItem1, result.get(1).getCartItem());
    }

    @Test
    public void loadWhenCartItemNetPriceIsLessThanZero() {
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem().setNetPrice(BigDecimal.valueOf(-1L));
        cart.setItems(Collections.singletonList(cartItem));
        CartQuoteContext context = new CartQuoteContext(cart);
        CouponDiscountLoader loader = CouponDiscountLoader.builder().context(context).soapClient(soapClient)
                .taskRunner(taskRunner).build();

        List<CartItemDiscounts> result = loader.load();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void loadWhenCouponCodesIsEmpty() {
        Cart cart = CartDataFactory.cart().setCouponCodes(Collections.EMPTY_SET);
        CartItem cartItem = CartDataFactory.cartItem().setCouponCodes(Collections.EMPTY_SET);
        cart.setItems(Collections.singletonList(cartItem));
        CartQuoteContext context = new CartQuoteContext(cart);
        CouponDiscountLoader loader = CouponDiscountLoader.builder().context(context).soapClient(soapClient)
                .taskRunner(taskRunner).build();

        List<CartItemDiscounts> result = loader.load();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void loadWhenFindDiscountsBySOAPIsEmpty() {
        CartQuoteContext context = new CartQuoteContext(CartDataFactory.cart());
        Mockito.when(productOMSEndpoint.findLatestDiscountsByProductIdAndCouponCodes(any(), any(), any()))
                .thenReturn(Collections.emptyList());
        CouponDiscountLoader loader = CouponDiscountLoader.builder().context(context).soapClient(soapClient)
                .taskRunner(taskRunner).build();

        List<CartItemDiscounts> result = loader.load();
        Assert.assertEquals(0, result.size());
    }

    private Discount buildDiscount() {
        Discount discount = new Discount();
        discount.setAmount(BigDecimal.ONE);
        discount.setAmountType(AmountType.FLAT);
        return discount;
    }
}
