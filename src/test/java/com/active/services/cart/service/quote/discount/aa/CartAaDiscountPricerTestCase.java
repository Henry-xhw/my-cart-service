package com.active.services.cart.service.quote.discount.aa;

import com.active.services.ProductType;
import com.active.services.cart.BaseTestCase;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.AmountType;
import com.active.services.product.Product;
import com.active.services.product.api.omsOnly.soap.ProductOMSEndpoint;
import com.active.services.product.discount.aa.AaDiscount;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class CartAaDiscountPricerTestCase extends BaseTestCase {

    private SOAPClient soapClient;

    private ProductOMSEndpoint productOMSEndpoint;

    //private AccountServiceSOAPEndPoint accountServiceSOAPEndPoint;

    private AaDiscount aaDiscount;

    @Before
    public void setUp() {
        super.setUp();
        productOMSEndpoint = Mockito.mock(ProductOMSEndpoint.class);
        //accountServiceSOAPEndPoint = Mockito.mock(AccountServiceSOAPEndPoint.class);
        soapClient = Mockito.mock(SOAPClient.class);
        Mockito.when(soapClient.getProductOMSEndpoint()).thenReturn(productOMSEndpoint);
        //Mockito.when(soapClient.accountServiceSOAPEndPoint()).thenReturn(accountServiceSOAPEndPoint);
        aaDiscount = new AaDiscount();
        aaDiscount.setAmount(BigDecimal.valueOf(10));
        aaDiscount.setAmountType(AmountType.FLAT);
        aaDiscount.setCurrency("USD");
        aaDiscount.setName("test Aa Discount");
        aaDiscount.setId(1L);
        aaDiscount.setMaximumAmount(BigDecimal.ONE);
    }

    @Test
    public void testQuoteWhenContextIsNull() {
        CartAaDiscountPricer cartAaDiscountPricer = new CartAaDiscountPricer(soapClient);
        cartAaDiscountPricer.quote(null);
        Mockito.verify(soapClient, times(0)).getProductOMSEndpoint();
    }

    @Test
    public void testQuoteWhenContextCartIsNull() {
        CartQuoteContext context = new CartQuoteContext(null);
        CartAaDiscountPricer cartAaDiscountPricer = new CartAaDiscountPricer(soapClient);
        cartAaDiscountPricer.quote(context);
        Mockito.verify(soapClient, times(0)).getProductOMSEndpoint();
    }

    @Test
    public void testQuoteWhenCartItemsIsEmpty() {
        Cart cart = CartDataFactory.cart();
        cart.setItems(new ArrayList<>());
        CartQuoteContext context = new CartQuoteContext(cart);
        CartAaDiscountPricer cartAaDiscountPricer = new CartAaDiscountPricer(soapClient);
        cartAaDiscountPricer.quote(context);
        Mockito.verify(soapClient, times(0)).getProductOMSEndpoint();
    }

    @Test
    public void testQuoteWhenAaDiscountIsNull() {
        Cart cart = CartDataFactory.cart();
        CartQuoteContext context = new CartQuoteContext(cart);
        context.setAaMember(true);
        CartAaDiscountPricer cartAaDiscountPricer = new CartAaDiscountPricer(soapClient);
        cartAaDiscountPricer.quote(context);
        Mockito.verify(productOMSEndpoint, times(1)).findLatestAaDiscountByCurrencyCode(any(), any());
    }

    @Test
    public void testQuoteWhenNotExistAaProduct() {
        Cart cart = CartDataFactory.cart();
        cart.getItems().get(0).setSubItems(Collections.singletonList(CartDataFactory.cartItem()));
        List<Product> products = cart.getFlattenCartItems().stream().map(CartItem::getProductId)
                .map(this::getProduct).collect(Collectors.toList());
        products.stream().forEach(product -> product.setProductType(ProductType.BOOKING_PROTECTION));
        Mockito.when(productOMSEndpoint.findLatestAaDiscountByCurrencyCode(any(), any())).thenReturn(aaDiscount);
        CartQuoteContext context = new CartQuoteContext(cart);
        context.setAaMember(false);
        CartAaDiscountPricer cartAaDiscountPricer = new CartAaDiscountPricer(soapClient);
        cartAaDiscountPricer.quote(context);
        Mockito.verify(productOMSEndpoint, times(0)).findLatestAaDiscountByCurrencyCode(any(), any());
    }

    @Test
    public void testQuoteSuccess() {
        Cart cart = CartDataFactory.cart();
        cart.getFlattenCartItems().forEach(cartItem -> {
            CartItemFee cartItemFee = CartItemFee.builder()
                    .type(CartItemFeeType.PROCESSING_FLAT)
                    .unitPrice(BigDecimal.ONE)
                    .dueAmount(BigDecimal.ONE)
                    .units(3)
                    .name("processing fee")
                    .transactionType(FeeTransactionType.DEBIT)
                    .description("processing fee")
                    .build();
            cartItem.getFees().get(0).addSubItemFee(Arrays.asList(cartItemFee));
        });
        CartQuoteContext context = new CartQuoteContext(cart);
        context.setAaMember(false);
        List<Product> products = cart.getFlattenCartItems().stream().map(CartItem::getProductId)
                .map(this::getProduct).collect(Collectors.toList());
        products.get(0).setProductType(ProductType.AA_MEMBERSHIP);
        products.get(1).setProductType(ProductType.REGISTRATION);
        context.setProducts(products);
        CartAaDiscountPricer cartAaDiscountPricer = new CartAaDiscountPricer(soapClient);
        Mockito.when(productOMSEndpoint.findLatestAaDiscountByCurrencyCode(any(), any())).thenReturn(aaDiscount);
        cartAaDiscountPricer.quote(context);
        Mockito.verify(productOMSEndpoint, times(1)).findLatestAaDiscountByCurrencyCode(any(), any());
    }


    private Product getProduct(Long id) {
        Product product = new Product();
        product.setId(id);
        return product;
    }
}
