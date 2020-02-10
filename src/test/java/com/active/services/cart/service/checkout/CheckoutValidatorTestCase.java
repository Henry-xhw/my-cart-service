package com.active.services.cart.service.checkout;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeAllocation;
import com.active.services.cart.service.CartStatus;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CheckoutValidatorTestCase {

    @Test(expected = CartException.class)
    public void testFlattenCartItemsIsEmpty() {
        Cart cart = CartDataFactory.cart();
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);
        cart.setItems(new ArrayList<>());
        CheckoutValidator validator = new CheckoutValidator();
        validator.validate(checkoutContext);
    }

    @Test(expected = CartException.class)
    public void testPriceVersionIsMatch() {
        Cart cart = CartDataFactory.cart();
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);
        cart.setVersion(cart.getPriceVersion() + 1);
        CheckoutValidator validator = new CheckoutValidator();
        validator.validate(checkoutContext);
    }

    @Test(expected = CartException.class)
    public void testCartIsFinalized() {
        Cart cart = CartDataFactory.cart();
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);
        cart.setCartStatus(CartStatus.FINALIZED);
        CheckoutValidator validator = new CheckoutValidator();
        validator.validate(checkoutContext);
    }

    @Test
    public void testFeeAllocationIsEmpty() {
        Cart cart = CartDataFactory.cart();
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);
        CheckoutValidator validator = new CheckoutValidator();
        validator.validate(checkoutContext);
    }

    @Test(expected = CartException.class)
    public void testCartItemFeeIsEmpty() {
        Cart cart = CartDataFactory.cart();
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);

        List<CartItemFeeAllocation> feeAllocations = new ArrayList<>();
        CartItemFeeAllocation feeAllocation = new CartItemFeeAllocation();
        feeAllocation.setCartItemFeeIdentifier(cart.getItems().get(0).getFees().get(0).getIdentifier());
        feeAllocation.setAmount(BigDecimal.ONE);
        feeAllocations.add(feeAllocation);
        checkoutContext.setFeeAllocations(feeAllocations);

        List<CartItem> cartItems = cart.getFlattenCartItems();
        for (CartItem cartItem : cartItems) {
            cartItem.setFees(Collections.emptyList());
        }

        CheckoutValidator validator = new CheckoutValidator();
        validator.validate(checkoutContext);
    }

    @Test(expected = CartException.class)
    public void testFeeAllocationItemIsMatched() {
        Cart cart = CartDataFactory.cart();
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);

        List<CartItemFeeAllocation> feeAllocations = new ArrayList<>();
        CartItemFeeAllocation feeAllocation = new CartItemFeeAllocation();
        feeAllocation.setCartItemFeeIdentifier(cart.getItems().get(0).getFees().get(0).getIdentifier());
        feeAllocation.setAmount(BigDecimal.ONE);
        feeAllocations.add(feeAllocation);
        checkoutContext.setFeeAllocations(feeAllocations);
        CheckoutValidator validator = new CheckoutValidator();
        validator.validate(checkoutContext);
    }

    @Test(expected = CartException.class)
    public void testFeeAllocationAmountGraterUnitPrice() {
        Cart cart = CartDataFactory.cart();
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);

        List<CartItemFee> cartItemFees = cart.getFlattenCartItems().stream()
                .filter(item -> Objects.nonNull(item.getFees())).map(CartItem::getFlattenCartItemFees)
                .flatMap(List::stream).collect(Collectors.toList());

        List<CartItemFeeAllocation> feeAllocations = new ArrayList<>();
        cartItemFees.stream().forEach(fee -> {
            CartItemFeeAllocation feeAllocation = new CartItemFeeAllocation();
            feeAllocation.setCartItemFeeIdentifier(fee.getIdentifier());
            feeAllocation.setAmount(BigDecimal.ONE.add(fee.getUnitPrice()));
            feeAllocations.add(feeAllocation);
        });
        checkoutContext.setFeeAllocations(feeAllocations);

        CheckoutValidator validator = new CheckoutValidator();
        validator.validate(checkoutContext);
    }

    @Test
    public void testFeeAllocationAmountEqDueAmount() {
        Cart cart = CartDataFactory.cart();
        CheckoutContext checkoutContext = new CheckoutContext();
        checkoutContext.setCart(cart);

        List<CartItemFee> cartItemFees = cart.getFlattenCartItems().stream()
                .filter(item -> Objects.nonNull(item.getFees())).map(CartItem::getFlattenCartItemFees)
                .flatMap(List::stream).collect(Collectors.toList());


        List<CartItemFeeAllocation> feeAllocations = new ArrayList<>();
        cartItemFees.stream().forEach(fee -> {
            fee.setDueAmount(fee.getUnitPrice());
            CartItemFeeAllocation feeAllocation = new CartItemFeeAllocation();
            feeAllocation.setCartItemFeeIdentifier(fee.getIdentifier());
            feeAllocation.setAmount(fee.getDueAmount());
            feeAllocations.add(feeAllocation);
        });
        checkoutContext.setFeeAllocations(feeAllocations);

        CheckoutValidator validator = new CheckoutValidator();
        validator.validate(checkoutContext);
    }
}
