package com.active.services.cart.service;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeesInCart;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.repository.CartItemFeeRepository;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.cart.service.quote.CartPriceEngine;
import com.active.services.cart.service.validator.CreateCartItemsValidator;
import com.active.services.cart.util.DataAccess;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.PlatformTransactionManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.spy;

@RunWith(SpringRunner.class)
@PrepareForTest({CartService.class})
public class CartServiceTestCase extends BaseTestCase {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemFeeRepository cartItemFeeRepository;

    @Mock
    private CartPriceEngine cartPriceEngine;

    @Mock
    private DataAccess dataAccess;

    @InjectMocks
    private CartService cartService;

    @Before
    public void setUp() {
        cartService = spy(cartService);
        super.setUp();
    }

    @Test
    public void deleteSuccess() {
        cartService.delete(1L);
        super.tearDown();
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
    public void updateCartSuccess() {
        Cart cart = CartDataFactory.cart();
        when(cartRepository.getCart(any())).thenReturn(Optional.of(cart));
        cartService.update(cart);
        Mockito.verify(cartRepository).updateCart(cart);
    }

    @Test
    public void quoteSuccess() {
        PlatformTransactionManager mock = mock(PlatformTransactionManager.class);
        DataAccess dataAccess = new DataAccess(mock);
        CartService cartService = new CartService(cartRepository, cartItemFeeRepository, cartPriceEngine,
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
        PlatformTransactionManager mock = mock(PlatformTransactionManager.class);
        DataAccess dataAccess = new DataAccess(mock);
        CartService cartService = spy(new CartService(cartRepository, cartItemFeeRepository, cartPriceEngine,
                dataAccess));
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
        CartService cartService = new CartService(cartRepository, cartItemFeeRepository, cartPriceEngine,
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

        assertEquals(1, cartWithFullPrice.getItems().size());
        assertEquals(1, cartWithFullPrice.getItems().get(0).getFees().size());
        assertEquals(1, cartWithFullPrice.getItems().get(0).getFees().get(0).getSubItems().size());
    }

    @Test
    public void checkoutNullCart() {
        UUID cartId = UUID.randomUUID();
        when(cartRepository.getCart(cartId)).thenReturn(Optional.ofNullable(null));
        try {
            cartService.checkout(cartId, new CheckoutContext());
            fail("should fail when there is no cart");
        } catch (CartException e) {
            assertEquals(ErrorCode.CART_NOT_FOUND, e.getErrorCode());
        }
    }
}
