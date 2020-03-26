package com.active.services.cart.service.quote.discount.membership;

import com.active.services.ActiveEntityNotFoundException;
import com.active.services.ProductType;
import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountType;
import com.active.services.product.Product;
import com.active.services.product.ProductMembership;
import com.active.services.product.api.omsOnly.soap.ProductOMSEndpoint;
import com.active.services.product.api.omsOnly.types.FindLatestMembershipDiscountsByProductIdsRsp;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

public class CartMembershipPricerTestCase extends BaseTestCase {

    @InjectMocks
    private CartMembershipPricer cartMembershipPricer;

    @Mock
    private SOAPClient soapClient;

    @Mock
    private ProductOMSEndpoint productOMSEndpoint;



    private Long productId = RandomUtils.nextLong();

    private Long membershipId = RandomUtils.nextLong();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDoQuoteNoMembershipProducts() {
        CartQuoteContext context = buildCartQuoteContext();

        List<CartItem> cartItems = new ArrayList<>();
        cartMembershipPricer.doQuote(context, cartItems);
        assertThat(context.getAppliedDiscounts()).isEmpty();

        Product product = mock(Product.class);
        when(product.getId()).thenReturn(productId);
        when(product.getProductType()).thenReturn(ProductType.MEMBERSHIP);
        context.setProducts(Arrays.asList(product));
        cartMembershipPricer.doQuote(context, cartItems);
        assertThat(context.getAppliedDiscounts()).isEmpty();
    }

    @Test
    public void testDoQuoteNoMembershipDiscountsHistory() {
        CartQuoteContext context = buildCartQuoteContext();

        Product product = mock(Product.class);
        when(product.getId()).thenReturn(productId);
        when(product.getProductType()).thenReturn(ProductType.REGISTRATION);
        context.setProducts(Arrays.asList(product));
        when(soapClient.getProductOMSEndpoint()).thenReturn(productOMSEndpoint);

        cartMembershipPricer.doQuote(context, context.getCart().getItems());
        assertThat(context.getAppliedDiscounts()).isEmpty();
    }

    @Test
    public void testDoQuoteHasMembershipDiscountsHistory() throws ActiveEntityNotFoundException {
        CartQuoteContext context = buildCartQuoteContext();
        CartItem cartItem = context.getCart().getItems().get(0);
        cartItem.setMembershipId(membershipId);

        Product product = mock(Product.class);
        when(product.getId()).thenReturn(productId);
        when(product.getProductType()).thenReturn(ProductType.REGISTRATION);
        context.setProducts(Arrays.asList(product));
        when(soapClient.getProductOMSEndpoint()).thenReturn(productOMSEndpoint);

        FindLatestMembershipDiscountsByProductIdsRsp membershipDiscountRsp =
                mock(FindLatestMembershipDiscountsByProductIdsRsp.class);
        List<FindLatestMembershipDiscountsByProductIdsRsp> membershipDiscountRspList =
                Arrays.asList(membershipDiscountRsp);
        when(membershipDiscountRsp.getProductId()).thenReturn(productId);

        BigDecimal discountAmt = new BigDecimal("2.00");
        MembershipDiscountsHistory membershipDiscountHistory = mock(MembershipDiscountsHistory.class);
        when(membershipDiscountHistory.getMembershipId()).thenReturn(membershipId);
        when(membershipDiscountHistory.getAmountType()).thenReturn(AmountType.FLAT);
        when(membershipDiscountHistory.getAmount()).thenReturn(discountAmt);
        when(membershipDiscountHistory.getMembershipId()).thenReturn(membershipId);

        List<MembershipDiscountsHistory> membershipDiscountsHistories = Arrays.asList(membershipDiscountHistory);
        when(membershipDiscountRsp.getHistories()).thenReturn(membershipDiscountsHistories);

        Mockito.doReturn(membershipDiscountRspList).when(productOMSEndpoint)
                .findLatestMembershipDiscountsByProductIds(any(), any());

        ProductMembership productMembership = mock(ProductMembership.class);
        when(productMembership.getProductId()).thenReturn(productId);
        when(productMembership.getMembershipId()).thenReturn(membershipId);
        List<ProductMembership> productMemberships = Arrays.asList(productMembership);
        Mockito.doReturn(productMemberships).when(productOMSEndpoint).findProductMembershipsForProductIds(any(), any());

        MembershipDiscountContext mockMembershipContext = mock(MembershipDiscountContext.class);
        when(mockMembershipContext.getMembershipDiscountsHistory(productId)).thenReturn(membershipDiscountsHistories);
        CartItemMembershipPricer cartItemMembershipPricer = new CartItemMembershipPricer(mockMembershipContext);

        CartMembershipPricer spyCartPricer = spy(cartMembershipPricer);
        Mockito.doReturn(cartItemMembershipPricer).when(spyCartPricer).getCartItemMembershipPricer(any());

        spyCartPricer.doQuote(context, context.getCart().getItems());

        Predicate<Discount> membershipPredicate = d -> d.getDiscountType() == DiscountType.MEMBERSHIP &&
                d.getMembershipId().equals(membershipId);
        assertThat(context.getAppliedDiscounts()).hasSizeGreaterThan(0).anyMatch(membershipPredicate);
    }

    private CartQuoteContext buildCartQuoteContext() {
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setProductId(productId);

        Cart cart = CartDataFactory.cart();
        cart.setItems(Arrays.asList(cartItem));

        CartQuoteContext context = new CartQuoteContext(cart);
        return context;
    }

}
