package com.active.services.cart.service;

import com.active.services.cart.client.rest.OrderService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeesInCart;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.cart.model.PaymentType;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.cart.model.v1.req.CheckoutReq;
import com.active.services.cart.repository.CartItemFeeRepository;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.quote.CartPriceEngine;
import com.active.services.cart.service.validator.CreateCartItemsValidator;
import com.active.services.cart.util.DataAccess;
import com.active.services.order.management.api.v3.types.OrderResponseDTO;
import com.active.services.order.management.api.v3.types.PlaceOrderRsp;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CartService.class})
public class CartServiceTestCase {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemFeeRepository cartItemFeeRepository;

    @Mock
    private CartPriceEngine cartPriceEngine;

    @Mock
    private DataAccess dataAccess;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private CartService cartService;

    @Before
    public void setUp() {
        cartService = spy(cartService);
    }

    @Test
    public void deleteSuccess() {
        cartService.delete(1L);
    }

    @Test
    public void updateCartItemSuccess() {
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem);
        cart.setItems(list);
        when(cartRepository.getCart(cart.getIdentifier())).thenReturn(Optional.of(cart));
        try {
            cartService.updateCartItems(cart.getIdentifier(), list);
            Mockito.verify(cartRepository, times(1)).updateCartItems(list);
        }
        catch (CartException e) {

        }
    }

    @Test
    public void updateCartItemWithNotExistCartItemId() {
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem);
        cart.setItems(Collections.singletonList(CartDataFactory.cartItem()));
        when(cartRepository.getCart(cart.getIdentifier())).thenReturn(Optional.of(cart));
        try {
            cartService.updateCartItems(cart.getIdentifier(), list);
            Mockito.verify(cartRepository, times(0)).updateCartItems(list);
        }
        catch (CartException e) {

        }
    }

    @Test
    public void findCartsByIdentifierSuccess() {
        UUID identifier = UUID.randomUUID();
        Cart cart = CartDataFactory.cart();
        when(cartRepository.getCart(identifier)).thenReturn(Optional.of(cart));
        cartService.getCartByUuid(identifier);
        Mockito.verify(cartRepository).getCart(identifier);
    }

    @Test(expected = CartException.class)
    public void findCartsWhenIdentifierIsNotExistThrowException() {
        UUID identifier = UUID.randomUUID();
        when(cartRepository.getCart(identifier)).thenReturn(Optional.ofNullable(null));
        cartService.getCartByUuid(identifier);
        Mockito.verify(cartRepository).getCart(identifier);
    }

    @Test
    public void searchUUIDsByOwnerIdSuccess() {
        UUID ownerId = UUID.randomUUID();
        List<UUID> cartIds = new ArrayList<>();
        cartIds.add(UUID.randomUUID());
        when(cartRepository.search(ownerId)).thenReturn(cartIds);
        cartService.search(ownerId);
        Mockito.verify(cartRepository).search(ownerId);
    }

    @Test
    public void deleteCartItemSuccess() {
        Cart cart = CartDataFactory.cart();
        Optional<CartItem> cartItem = Optional.of(CartDataFactory.cartItem());
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem.get());
        cart.setItems(list);
        UUID cartItemId = cartItem.get().getIdentifier();
        try {
            cartService.deleteCartItem(cart, cartItemId);
            Mockito.verify(cartRepository).batchDeleteCartItems(any());
        } catch (Exception e) {

        }
    }

    @Test
    public void deleteCartItemFailedWithNotExistCartItemId() {
        Cart cart = CartDataFactory.cart();
        Optional<CartItem> cartItem = Optional.of(CartDataFactory.cartItem());
        List<CartItem> list = new ArrayList<>();
        list.add(cartItem.get());
        cart.setItems(list);
        UUID cartItemId = UUID.randomUUID();
        try {
            cartService.deleteCartItem(cart, cartItemId);
            Mockito.verify(cartRepository, times(0)).batchDeleteCartItems(any());
        } catch (Exception e) {

        }
    }

    @Test
    public void createCartSuccess() {
        Cart cart = CartDataFactory.cart();
        cartService.create(cart);
        Mockito.verify(cartRepository).createCart(cart);
    }

    @Test
    public void quoteSuccess() {
        PlatformTransactionManager mock = mock(PlatformTransactionManager.class);
        DataAccess dataAccess = new DataAccess(mock);
        CartService cartService = new CartService(cartRepository, orderService, cartItemFeeRepository, cartPriceEngine,
                dataAccess);
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setId(1L);
        CartItemFee cartItemFee = CartDataFactory.cartItemFee();
        cartItem.setFees(Arrays.asList(cartItemFee));
        cart.setItems(Arrays.asList(cartItem));
        UUID identifier = cart.getIdentifier();
        when(cartRepository.getCart(identifier)).thenReturn(Optional.of(cart));
        Cart quote = cartService.quote(identifier);
        Mockito.verify(cartItemFeeRepository).deleteLastQuoteResult(cartItem.getId());
        Mockito.verify(cartItemFeeRepository).createCartItemFee(cartItemFee);
        Mockito.verify(cartItemFeeRepository).createCartItemCartItemFee(any());
    }

    @Test
    public void insertCartItemSuccess() {
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        CartItem childCartItem = CartDataFactory.cartItem();
        cartItem.setIdentifier(UUID.randomUUID());
        childCartItem.setIdentifier(null);
        List<CartItem> childCartItemList = new ArrayList<>();
        childCartItemList.add(childCartItem);
        cartItem.setSubItems(childCartItemList);
        List<CartItem> cartItemList = new ArrayList<>();
        cartItemList.add(cartItem);
        cart.setItems(cartItemList);
        CreateCartItemsValidator createCartItemsValidator = spy(new CreateCartItemsValidator(cart, cartItemList));
        doNothing().when(createCartItemsValidator).validate();
        when(cartService.getCartItemsValidator(cart, cartItemList)).thenReturn(createCartItemsValidator);
        when(cartRepository.getCartItemIdByCartItemUuid(cartItem.getIdentifier())).thenReturn(Optional.ofNullable(cart.getId()));
        when(cartRepository.getCart(cart.getIdentifier())).thenReturn(Optional.of(cart));
        cartService.insertCartItems(cart.getIdentifier(), cartItemList);
    }

    @Test
    public void getCartWithFullSuccess() throws Exception {
        PlatformTransactionManager mock = mock(PlatformTransactionManager.class);
        DataAccess dataAccess = new DataAccess(mock);
        CartService cartService = new CartService(cartRepository, orderService, cartItemFeeRepository, cartPriceEngine,
                dataAccess);
        Cart cart = CartDataFactory.cart();
        CartItem cartItem = CartDataFactory.cartItem();
        cartItem.setId(1L);
        cart.setItems(Arrays.asList(cartItem));
        UUID identifier = cart.getIdentifier();
        when(cartRepository.getCart(identifier)).thenReturn(Optional.of(cart));
        CartItemFeesInCart cartItemFee1 = new CartItemFeesInCart();
        cartItemFee1.setCartItemId(cartItem.getId());
        cartItemFee1.setId(1L);
        cartItemFee1.setParentId(null);
        CartItemFeesInCart cartItemFee2 = new CartItemFeesInCart();
        cartItemFee2.setCartItemId(cartItem.getId());
        cartItemFee2.setId(2L);
        cartItemFee2.setParentId(1L);
        List<CartItemFeesInCart> fees = new ArrayList<>();
        fees.add(cartItemFee1);
        fees.add(cartItemFee2);
        when(cartItemFeeRepository.getCartItemFeesByCartId(cart.getId())).thenReturn(fees);

        Class cl = cartService.getClass();
        Method method = cl.getDeclaredMethod("getCartWithFullPriceByUuid", new Class[]{UUID.class});

        method.setAccessible(true);

        Cart cartWithFullPrice = (Cart) method.invoke(cartService, new Object[]{identifier});

        Assert.assertEquals(1, cartWithFullPrice.getItems().size());
        Assert.assertEquals(1, cartWithFullPrice.getItems().get(0).getFees().size());
        Assert.assertEquals(1, cartWithFullPrice.getItems().get(0).getFees().get(0).getSubItems().size());
    }

    @Test
    public void checkoutNullCart() {
        UUID cartId = UUID.randomUUID();
        when(cartRepository.getCart(cartId)).thenReturn(Optional.ofNullable(null));
        try {
            cartService.checkout(cartId, new CheckoutReq());
            fail("should fail when there is no cart");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void checkoutWhenCartWithoutCartItem() {
        UUID cartId = UUID.randomUUID();
        Cart cart = getQualifiedCart(cartId);
        cart.setItems(new ArrayList<>());
        when(cartItemFeeRepository.getCartItemFeesByCartId(cart.getId())).thenReturn(new ArrayList<>());
        when(cartRepository.getCart(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.acquireLock(any(), anyString())).thenReturn(1);
        try {
            cartService.checkout(cartId, new CheckoutReq());
            fail("should fail when there is no cart");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_ITEM_NOT_FOUND, e.getErrorCode());
        }
    }

    @Test
    public void checkoutWhenCartWithUnMatchedPricing() {
        UUID cartId = UUID.randomUUID();
        Cart cart = getQualifiedCart(cartId);
        cart.setVersion(3);
        when(cartItemFeeRepository.getCartItemFeesByCartId(cart.getId())).thenReturn(new ArrayList<>());
        when(cartRepository.getCart(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.acquireLock(any(), anyString())).thenReturn(0);
        try {
            cartService.checkout(cartId, new CheckoutReq());
            fail("should fail when there is no cart");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_PRICING_OUT_OF_DATE, e.getErrorCode());
        }
    }

    @Test
    public void checkoutWhenLockCartFailed() {
        UUID cartId = UUID.randomUUID();
        Cart cart = getQualifiedCart(cartId);
        when(cartItemFeeRepository.getCartItemFeesByCartId(cart.getId())).thenReturn(new ArrayList<>());
        when(cartRepository.getCart(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.acquireLock(any(), anyString())).thenReturn(0);
        try {
            cartService.checkout(cartId, new CheckoutReq());
            fail("should fail when there is no cart");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_LOCKED, e.getErrorCode());
        }
    }

    @Test
    public void checkout() {
        UUID cartId = UUID.randomUUID();
        Cart cart = getQualifiedCart(cartId);
        when(cartItemFeeRepository.getCartItemFeesByCartId(cart.getId())).thenReturn(new ArrayList<>());
        when(cartRepository.getCart(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.acquireLock(any(), anyString())).thenReturn(1);
        Long orderId = RandomUtils.nextLong();
        CheckoutReq req = getCheckoutReq();
        when(orderService.placeOrder(any())).thenReturn(buildRsp(orderId));
        try {
            List<CheckoutResult> results = cartService.checkout(cartId, req);
            assertTrue(CollectionUtils.isNotEmpty(results));
            assertEquals(orderId, results.get(0).getOrderId());
        } catch (CartException e) {
            fail("no exception");
        }
    }

    private CheckoutReq getCheckoutReq() {
        CheckoutReq req = new CheckoutReq();
        PaymentAccount paymentAccount = new PaymentAccount();
        paymentAccount.setAmsAccountId("2323232");
        paymentAccount.setPaymentType(PaymentType.CREDIT_CARD);
        req.setPaymentAccount(paymentAccount);
        req.setOrderUrl("www.active.com");
        req.setSendReceipt(true);
        return req;
    }

    private PlaceOrderRsp buildRsp(Long orderId) {
        PlaceOrderRsp rsp = new PlaceOrderRsp();
        OrderResponseDTO dto = OrderResponseDTO.builder().orderId(orderId).build();
        List<OrderResponseDTO> list = new ArrayList<>();
        list.add(dto);
        rsp.setOrderResponses(list);
        return rsp;
    }

    private Cart getQualifiedCart(UUID cartId) {
        Cart cart = CartDataFactory.cart();
        cart.setIdentifier(cartId);
        List<CartItem> items = new ArrayList<>();
        items.add(CartDataFactory.cartItem());
        cart.setItems(items);
        cart.setVersion(1);
        cart.setPriceVersion(1);
        return cart;
    }
}
