package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.cart.service.checkout.commit.CheckoutCommitPhaseProcessor;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.Discount;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;
import com.active.services.product.nextgen.v1.rsp.GetDiscountUsageRsp;

import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CartCouponPricerTestCase {

    private CartCouponPricer cartCouponPricer = Mockito.mock(CartCouponPricer.class);

    private CouponDiscountLoader couponDiscountLoader = Mockito.mock(CouponDiscountLoader.class);

    private CartItemCouponPricer cartItemCouponPricer = Mockito.mock(CartItemCouponPricer.class);

    private ProductService productService = Mockito.mock(ProductService.class);

    @Before
    public void setUp() {
        when(cartCouponPricer.getCouponDiscountLoader(any(), any())).thenReturn(couponDiscountLoader);
        when(cartCouponPricer.getCartItemCouponPricer(any(), any())).thenReturn(cartItemCouponPricer);
        ReflectionTestUtils.setField(cartCouponPricer, "productService", productService);
    }

    @Test
    public void doQuoteSuccess() {
        Map<CartItem, List<Discount>> cartItemCoupons = new HashMap<>();
        CartItem cartItem1 = CartDataFactory.cartItem().setQuantity(1);
        Discount discount1 = new Discount();
        discount1.setUsageLimit(1);
        cartItemCoupons.put(cartItem1, Lists.list(discount1));
        CartItem cartItem2 = CartDataFactory.cartItem().setQuantity(2);
        Discount discount2 = new Discount();
        discount2.setUsageLimit(-1);
        cartItemCoupons.put(cartItem2, Lists.list(discount2));
        when(couponDiscountLoader.loadDiscounts()).thenReturn(cartItemCoupons);

        GetDiscountUsageRsp getDiscountUsageRsp = new GetDiscountUsageRsp();
        getDiscountUsageRsp.setDiscountUsages(Lists.list(new DiscountUsage()));
        when(productService.getDiscountUsages(any())).thenReturn(getDiscountUsageRsp);

        Mockito.doCallRealMethod().when(cartCouponPricer).doQuote(any(), anyList());
        CartQuoteContext cartQuoteContext = new CartQuoteContext(CartDataFactory.cart());
        cartCouponPricer.doQuote(cartQuoteContext, Lists.list(cartItem1, cartItem2));

        verify(productService, times(1)).getDiscountUsages(any());
        ArgumentCaptor<CouponDiscountContext> argumentCaptor1 = ArgumentCaptor.forClass(CouponDiscountContext.class);
        ArgumentCaptor<List> argumentCaptor2 = ArgumentCaptor.forClass(List.class);
        verify(cartCouponPricer, times(2))
                .getCartItemCouponPricer(argumentCaptor1.capture(), argumentCaptor2.capture());
        assertEquals(getDiscountUsageRsp.getDiscountUsages(), argumentCaptor1.getValue().getDiscountUsages());
        assertEquals(cartItemCoupons.get(cartItem2), argumentCaptor2.getAllValues().get(0));
        assertEquals(cartItemCoupons.get(cartItem1), argumentCaptor2.getAllValues().get(1));
    }

    @Test
    public void doQuoteWhenDiscountsIsEmpty() {
        when(couponDiscountLoader.loadDiscounts()).thenReturn(new HashMap<>());

        Mockito.doCallRealMethod().when(cartCouponPricer).doQuote(any(), anyList());
        CartQuoteContext cartQuoteContext = new CartQuoteContext(CartDataFactory.cart());
        cartCouponPricer.doQuote(cartQuoteContext, Lists.emptyList());

        verify(productService, times(0)).getDiscountUsages(any());
        verify(cartCouponPricer, times(0)).getCartItemCouponPricer(any(), anyList());
    }

    @Test
    public void doQuoteWhenLimitedDiscountsIsEmpty() {
        Map<CartItem, List<Discount>> cartItemCoupons = new HashMap<>();
        CartItem cartItem1 = CartDataFactory.cartItem().setQuantity(1);
        Discount discount1 = new Discount();
        discount1.setUsageLimit(-1);
        cartItemCoupons.put(cartItem1, Lists.list(discount1));
        when(couponDiscountLoader.loadDiscounts()).thenReturn(cartItemCoupons);

        Mockito.doCallRealMethod().when(cartCouponPricer).doQuote(any(), anyList());
        CartQuoteContext cartQuoteContext = new CartQuoteContext(CartDataFactory.cart());
        cartCouponPricer.doQuote(cartQuoteContext, Lists.list(cartItem1));

        verify(productService, times(0)).getDiscountUsages(any());
    }

    @Test
    public void getCartItemCouponPricerSuccess() {
        Assert.assertNull(new CartCouponPricer().getCartItemCouponPricer(new CouponDiscountContext(),
                Lists.emptyList()));
    }

    @Test
    public void getCheckoutInventoryProcessorSuccess() {
        Assert.assertNull(new CartCouponPricer().getCouponDiscountLoader(new CartQuoteContext(CartDataFactory.cart()),
                Lists.emptyList()));
    }
}
