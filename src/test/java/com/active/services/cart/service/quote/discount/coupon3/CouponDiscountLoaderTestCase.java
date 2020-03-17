package com.active.services.cart.service.quote.discount.coupon3;

import com.active.platform.concurrent.ExecutorServiceTaskRunner;
import com.active.platform.concurrent.TaskRunner;
import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.coupon.CartItemDiscounts;
import com.active.services.product.AmountType;
import com.active.services.product.Discount;
import com.active.services.product.api.omsOnly.soap.ProductOMSEndpoint;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;
import com.active.services.product.nextgen.v1.rsp.GetDiscountUsageRsp;

import io.micrometer.core.instrument.util.NamedThreadFactory;
import org.apache.commons.lang.math.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.when;

public class CouponDiscountLoaderTestCase extends BaseTestCase {

    @Mock
    private SOAPClient soapClient;

    private TaskRunner taskRunner;
    @Mock
    private ProductOMSEndpoint productOMSEndpoint;
    @Mock
    private ProductService productService;

    @Before
    public void setUp() {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        taskRunner = new ExecutorServiceTaskRunner(new ThreadPoolExecutor(5, 100,
                60L, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),
                new NamedThreadFactory("cart-backend-compute-thread-")));
        Mockito.when(soapClient.getProductOMSEndpoint()).thenReturn(productOMSEndpoint);
    }

    @Test
    public void loadSuccess() {
        Cart cart = CartDataFactory.cart();
        CartItem cartItem1 = CartDataFactory.cartItem().setNetPrice(BigDecimal.valueOf(1L));
        CartItem cartItem2 = CartDataFactory.cartItem().setNetPrice(BigDecimal.valueOf(2L));
        cart.setItems(Arrays.asList(cartItem1, cartItem2));
        CartQuoteContext context = new CartQuoteContext(cart);
        Discount discount = buildDiscount();
        when(productOMSEndpoint.findLatestDiscountsByProductIdAndCouponCodes(any(), any(), any()))
                .thenReturn(Collections.singletonList(discount));

        List<Long> prodIds =
                cart.getFlattenCartItems().stream().map(ci -> ci.getProductId()).collect(Collectors.toList());

        GetDiscountUsageRsp rsp = new GetDiscountUsageRsp();
        List<DiscountUsage> discountUsageList = Collections.singletonList(getDiscountUsage(discount.getId(), 2, 1));
        rsp.setDiscountUsages(discountUsageList);

        when(productService.getDiscountUsages(any())).thenReturn(rsp);
        CouponDiscountLoader loader = CouponDiscountLoader.builder().context(context).soapClient(soapClient)
                .taskRunner(taskRunner).productService(productService).build();

        List<CartItemDiscounts> result = loader.load();
        Assert.assertTrue(findMatchItemByAmount(cart.getItems(), result.get(0).getCartItem()));
        Assert.assertTrue(findMatchItemByAmount(cart.getItems(), result.get(1).getCartItem()));
    }

    @Test
    public void loadDiscountWithoutUsageLimit() {
        Cart cart = CartDataFactory.cart();
        CartItem cartItem1 = CartDataFactory.cartItem().setNetPrice(BigDecimal.valueOf(1L));
        CartItem cartItem2 = CartDataFactory.cartItem().setNetPrice(BigDecimal.valueOf(2L));
        cart.setItems(Arrays.asList(cartItem1, cartItem2));
        CartQuoteContext context = new CartQuoteContext(cart);
        Discount discount = buildDiscount();
        when(productOMSEndpoint.findLatestDiscountsByProductIdAndCouponCodes(any(), any(), any()))
                .thenReturn(Collections.singletonList(discount));

        List<Long> prodIds =
                cart.getFlattenCartItems().stream().map(ci -> ci.getProductId()).collect(Collectors.toList());

        GetDiscountUsageRsp rsp = new GetDiscountUsageRsp();
        List<DiscountUsage> discountUsageList = Collections.singletonList(getDiscountUsage(discount.getId(), 2, 2));
        rsp.setDiscountUsages(discountUsageList);

        when(productService.getDiscountUsages(any())).thenReturn(rsp);
        CouponDiscountLoader loader = CouponDiscountLoader.builder().context(context).soapClient(soapClient)
                .taskRunner(taskRunner).productService(productService).build();

        List<CartItemDiscounts> result = loader.load();
        Assert.assertEquals(0, result.size());
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
        discount.setId(RandomUtils.nextLong());
        return discount;
    }

    private boolean findMatchItemByAmount(List<CartItem> cartItems, CartItem cartItem) {
        return cartItems.stream().anyMatch(item -> item.getNetPrice().compareTo(cartItem.getNetPrice()) == 0);
    }

    private DiscountUsage getDiscountUsage(Long discountId, Integer limit, Integer usage) {
        DiscountUsage discountUsage = new DiscountUsage();
        discountUsage.setDiscountId(discountId);
        discountUsage.setLimit(limit);
        discountUsage.setUsage(usage);
        return discountUsage;
    }
}
