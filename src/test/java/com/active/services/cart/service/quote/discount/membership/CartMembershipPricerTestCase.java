package com.active.services.cart.service.quote.discount.membership;

import com.active.services.ActiveEntityNotFoundException;
import com.active.services.Context;
import com.active.services.ContextWrapper;
import com.active.services.ProductType;
import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.Product;
import com.active.services.product.ProductMembership;
import com.active.services.product.api.omsOnly.soap.ProductOMSEndpoint;
import com.active.services.product.api.omsOnly.types.FindLatestMembershipDiscountsByProductIdsRsp;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CartMembershipPricerTestCase extends BaseTestCase {

    private CartMembershipPricer cartMembershipPricer;

    private SOAPClient soapClient = mock(SOAPClient.class);

    private CartQuoteContext context = mock(CartQuoteContext.class);

    private ProductOMSEndpoint productOMSEndpoint = mock(ProductOMSEndpoint.class);

    private Long productId = 1L;

    private Long membershipId = 12345L;

    @Before
    public void setUp() {
        this.cartMembershipPricer = new CartMembershipPricer();
        ReflectionTestUtils.setField(cartMembershipPricer, "soapClient", soapClient);
    }

    @Test
    public void testDoQuoteNoMembershipProducts() {
        List<CartItem> cartItems = new ArrayList<>();
        cartMembershipPricer.doQuote(context, cartItems);


        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getProductId()).thenReturn(productId);

        Map<Long, Product> productIdMap = new HashMap<>();
        Product product = mock(Product.class);
        productIdMap.put(productId, product);
        when(product.getProductType()).thenReturn(ProductType.MEMBERSHIP);
        when(context.getProductsMap()).thenReturn(productIdMap);

        cartItems.add(cartItem);
        cartMembershipPricer.doQuote(context, cartItems);
    }

    @Test
    public void testDoQuoteNoMembershipDiscountsHistory() {
        List<CartItem> cartItems = new ArrayList<>();

        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getProductId()).thenReturn(productId);

        Map<Long, Product> productIdMap = new HashMap<>();
        Product product = mock(Product.class);
        productIdMap.put(productId, product);
        when(product.getProductType()).thenReturn(ProductType.REGISTRATION);
        when(context.getProductsMap()).thenReturn(productIdMap);

        cartItems.add(cartItem);

        when(soapClient.getProductOMSEndpoint()).thenReturn(productOMSEndpoint);

        cartMembershipPricer.doQuote(context, cartItems);
    }

    @Test
    public void testDoQuoteHasMembershipDiscountsHistory() throws ActiveEntityNotFoundException {
        List<CartItem> cartItems = new ArrayList<>();

        CartItem cartItem = mock(CartItem.class);
        when(cartItem.getProductId()).thenReturn(productId);

        Map<Long, Product> productIdMap = new HashMap<>();
        Product product = mock(Product.class);
        productIdMap.put(productId, product);
        when(product.getProductType()).thenReturn(ProductType.REGISTRATION);
        when(context.getProductsMap()).thenReturn(productIdMap);

        cartItems.add(cartItem);

        when(soapClient.getProductOMSEndpoint()).thenReturn(productOMSEndpoint);

        FindLatestMembershipDiscountsByProductIdsRsp membershipDiscountRsp =
                mock(FindLatestMembershipDiscountsByProductIdsRsp.class);

        List<FindLatestMembershipDiscountsByProductIdsRsp> membershipDiscountRspList =
                Arrays.asList(membershipDiscountRsp);

        when(membershipDiscountRsp.getProductId()).thenReturn(productId);

        MembershipDiscountsHistory membershipDiscountsHistory = mock(MembershipDiscountsHistory.class);
        when(membershipDiscountsHistory.getMembershipId()).thenReturn(membershipId);
        when(cartItem.getMembershipId()).thenReturn(membershipId);

        List<MembershipDiscountsHistory> membershipDiscountsHistories = Arrays.asList(membershipDiscountsHistory);
        when(membershipDiscountRsp.getHistories()).thenReturn(membershipDiscountsHistories);

        Mockito.doReturn(membershipDiscountRspList).when(productOMSEndpoint)
                .findLatestMembershipDiscountsByProductIds(any(), any());

        ProductMembership productMembership = mock(ProductMembership.class);
        when(productMembership.getProductId()).thenReturn(productId);
        when(productMembership.getMembershipId()).thenReturn(membershipId);
        List<ProductMembership> productMemberships = Arrays.asList(productMembership);
        Mockito.doReturn(productMemberships).when(productOMSEndpoint).findProductMembershipsForProductIds(any(), any());

        CartItemMembershipPricer cartItemMembershipPricer = mock(CartItemMembershipPricer.class);
        CartMembershipPricer spyCartMembershipPricer = spy(cartMembershipPricer);
        Mockito.doReturn(cartItemMembershipPricer).when(spyCartMembershipPricer).getCartItemMembershipPricer(any());

        spyCartMembershipPricer.doQuote(context, cartItems);
    }

}
