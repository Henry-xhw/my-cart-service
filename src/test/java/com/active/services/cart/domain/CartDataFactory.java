package com.active.services.cart.domain;

import com.active.platform.types.range.Range;
import com.active.services.cart.model.AdHocDiscountDto;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.model.v1.CreateCartItemDto;
import com.active.services.cart.model.v1.UpdateCartItemDto;
import com.active.services.cart.service.CartStatus;
import com.active.services.product.AmountType;
import com.active.services.product.DiscountType;

import org.apache.commons.lang3.RandomUtils;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static com.active.services.product.DiscountAlgorithm.MOST_EXPENSIVE;

public class CartDataFactory {

    public static Cart cart() {
        Cart cart = new Cart();

        cart.setId(1L);
        cart.setCurrencyCode("USD");
        cart.setKeyerId(UUID.randomUUID());
        cart.setOwnerId(UUID.randomUUID());
        cart.setIdentifier(UUID.randomUUID());
        cart.setItems(cartItems());
        cart.setCartStatus(CartStatus.CREATED);
        cart.setCouponCodes(Collections.singleton("FDSAFSA"));
        cart.setReservationGroupId(UUID.randomUUID());

        return cart;
    }

    public static Cart placeOrderCart() {
        Cart cart = new Cart();
        cart.setId(1L);
        cart.setCurrencyCode("USD");
        cart.setKeyerId(UUID.randomUUID());
        cart.setOwnerId(UUID.randomUUID());
        cart.setIdentifier(UUID.randomUUID());
        cart.setItems(placeOrderCartItems());
        cart.setCartStatus(CartStatus.CREATED);
        cart.setCouponCodes(Collections.singleton("FDSAFSA"));

        return cart;
    }

    private static List<CartItem> cartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem());
        cartItems.add(cartItem());
        return cartItems;
    }

    private static List<CartItem> placeOrderCartItems() {
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(placeOrderCartItem());
        cartItems.add(placeOrderCartItem());
        return cartItems;
    }

    public static CartItem cartItem() {
        return getCartItem(1, new BigDecimal(10), "description");
    }

    public static CartItem placeOrderCartItem() {
        return getPlaceOrderCartItem(1, new BigDecimal(10), "description");
    }

    public static UpdateCartItemDto updateCartItemDto(CartItem cartItem) {
        UpdateCartItemDto updateCartItemDto = new UpdateCartItemDto();
        updateCartItemDto.setBookingRange(cartItem.getBookingRange());
        updateCartItemDto.setGroupingIdentifier(cartItem.getGroupingIdentifier());
        updateCartItemDto.setIdentifier(cartItem.getIdentifier());
        updateCartItemDto.setProductDescription(cartItem.getProductDescription());
        updateCartItemDto.setProductId(cartItem.getProductId());
        updateCartItemDto.setQuantity(cartItem.getQuantity());
        updateCartItemDto.setOverridePrice(cartItem.getOverridePrice());
        updateCartItemDto.setTrimmedBookingRange(cartItem.getTrimmedBookingRange());
        updateCartItemDto.setProductName(cartItem.getProductName());
        updateCartItemDto.setCouponCodes(cartItem.getCouponCodes());
        updateCartItemDto.setReservationId(UUID.randomUUID());
        updateCartItemDto.setAdHocDiscounts(Arrays.asList(getAdHocDiscountDto()));
        return updateCartItemDto;
    }

    public static CartItemFee cartItemFee(BigDecimal price) {
        return getCartItemFee(FeeTransactionType.DEBIT, CartItemFeeType.PRICE, 1, price, "description",
                "name", null);
    }

    public static CartItemFeesInCart cartItemFeesInCart(BigDecimal price) {
        return getCartItemFeesInCart(FeeTransactionType.DEBIT, CartItemFeeType.PRICE, 1, price, "description",
                "name", null);
    }

    public static CartItemFee cartItemFee() {
        return cartItemFee(new BigDecimal(1));
    }

    public static CartItemFee getCartItemFee(FeeTransactionType transactionType, CartItemFeeType feeType, int unit,
                                              BigDecimal price,
                                              String description,
                                              String name,
                                              UUID relatedIdentifier) {
        CartItemFee cartItemFee = new CartItemFee();
        cartItemFee.setId(RandomUtils.nextLong());
        cartItemFee.setIdentifier(UUID.randomUUID());
        cartItemFee.setTransactionType(transactionType);
        cartItemFee.setType(feeType);
        cartItemFee.setUnits(unit);
        cartItemFee.setUnitPrice(price);
        cartItemFee.setDescription(description);
        cartItemFee.setName(name);
        cartItemFee.setDiscountIdentifier(relatedIdentifier);
        return cartItemFee;
    }

    public static CartItemFeesInCart getCartItemFeesInCart(FeeTransactionType transactionType, CartItemFeeType feeType,
                                                     int unit,
                                             BigDecimal price,
                                             String description,
                                             String name,
                                             UUID relatedIdentifier) {
        CartItemFeesInCart cartItemFeesInCart = new CartItemFeesInCart();
        cartItemFeesInCart.setId(RandomUtils.nextLong());
        cartItemFeesInCart.setIdentifier(UUID.randomUUID());
        cartItemFeesInCart.setTransactionType(transactionType);
        cartItemFeesInCart.setType(feeType);
        cartItemFeesInCart.setUnits(unit);
        cartItemFeesInCart.setUnitPrice(price);
        cartItemFeesInCart.setDescription(description);
        cartItemFeesInCart.setName(name);
        cartItemFeesInCart.setDiscountIdentifier(relatedIdentifier);
        return cartItemFeesInCart;
    }

    public static CartItem getCartItem(Integer quantity, BigDecimal price, String description) {
        CartItem cartItem = new CartItem();
        cartItem.setIdentifier(UUID.randomUUID());
        cartItem.setProductId(RandomUtils.nextLong());
        cartItem.setProductName("product name");
        cartItem.setProductDescription(description);
        Range<Instant> bookingRange = new Range<>();
        bookingRange.setLowerInclusive(Instant.now());
        bookingRange.setUpperExclusive(Instant.now());
        cartItem.setBookingRange(bookingRange);
        Range<Instant> trimmedBookingRange = new Range<>();
        trimmedBookingRange.setLowerInclusive(Instant.now());
        trimmedBookingRange.setUpperExclusive(Instant.now());
        cartItem.setTrimmedBookingRange(trimmedBookingRange);
        cartItem.setQuantity(quantity);
        cartItem.setOverridePrice(price);
        cartItem.setGroupingIdentifier("grouping identifier");
        cartItem.setFeeVolumeIndex(0);
        cartItem.setCouponCodes(Collections.singleton("FDSAFSA"));
        cartItem.setReservationId(UUID.randomUUID());
        List<CartItemFee> cartItemFees = new ArrayList<>();
        cartItemFees.add(cartItemFee(price));
        cartItem.setFees(cartItemFees);
        cartItem.setId(1L);
        return cartItem;
    }

    public static CartItem getPlaceOrderCartItem(Integer quantity, BigDecimal price, String description) {
        CartItem cartItem = new CartItem();
        cartItem.setIdentifier(UUID.randomUUID());
        cartItem.setProductId(RandomUtils.nextLong());
        cartItem.setProductName("product name");
        cartItem.setProductDescription(description);
        Range<Instant> bookingRange = new Range<>();
        bookingRange.setLowerInclusive(Instant.now());
        bookingRange.setUpperExclusive(Instant.now());
        cartItem.setBookingRange(bookingRange);
        Range<Instant> trimmedBookingRange = new Range<>();
        trimmedBookingRange.setLowerInclusive(Instant.now());
        trimmedBookingRange.setUpperExclusive(Instant.now());
        cartItem.setTrimmedBookingRange(trimmedBookingRange);
        cartItem.setQuantity(quantity);
        cartItem.setOverridePrice(price);
        cartItem.setGroupingIdentifier("grouping identifier");
        cartItem.setFeeVolumeIndex(0);
        cartItem.setCouponCodes(Collections.singleton("FDSAFSA"));
        List<CartItemFee> cartItemFees = new ArrayList<>();
        cartItemFees.add(cartItemFeesInCart(price));
        cartItem.setFees(cartItemFees);
        return cartItem;
    }

    public static Discount getDiscount(UUID relatedIdentifier) {
        Discount discount = new Discount();
        discount.setIdentifier(relatedIdentifier);
        discount.setAlgorithm(MOST_EXPENSIVE);
        discount.setAmount(BigDecimal.TEN);
        discount.setAmountType(AmountType.FLAT);
        discount.setApplyToRecurringBilling(false);
        discount.setCartId(1L);
        discount.setCouponCode("coiponCode");
        discount.setDescription("descprtion");
        discount.setDiscountId(333L);
        discount.setDiscountType(DiscountType.AD_HOC);
        discount.setName("name");
        discount.setCreatedBy("111");
        discount.setCreatedDt(Instant.now());
        discount.setModifiedBy("mmmm");
        discount.setModifiedDt(Instant.now());
        discount.setDiscountGroupId(66666L);
        return discount;
    }

    public static CreateCartItemDto getCreateCartItemDto() {
        CreateCartItemDto createCartItemDto = new CreateCartItemDto();
        createCartItemDto.setIdentifier(UUID.randomUUID());
        createCartItemDto.setProductId(RandomUtils.nextLong());
        createCartItemDto.setProductName("product name");
        createCartItemDto.setProductDescription("description");
        Range<Instant> bookingRange = new Range<>();
        bookingRange.setLowerInclusive(Instant.now());
        bookingRange.setUpperExclusive(Instant.now());
        createCartItemDto.setBookingRange(bookingRange);
        Range<Instant> trimmedBookingRange = new Range<>();
        trimmedBookingRange.setLowerInclusive(Instant.now());
        trimmedBookingRange.setUpperExclusive(Instant.now());
        createCartItemDto.setTrimmedBookingRange(trimmedBookingRange);
        createCartItemDto.setQuantity(1);
        createCartItemDto.setOverridePrice(BigDecimal.ONE);
        createCartItemDto.setGroupingIdentifier("grouping identifier");
        createCartItemDto.setFeeVolumeIndex(0);
        createCartItemDto.setCouponCodes(Collections.singleton("FDSAFSA"));
        createCartItemDto.setReservationId(UUID.randomUUID());
        createCartItemDto.setAdHocDiscounts(Arrays.asList(getAdHocDiscountDto()));
        return createCartItemDto;
    }

    public static AdHocDiscountDto getAdHocDiscountDto() {
        AdHocDiscountDto adHocDiscountDto = new AdHocDiscountDto();
        adHocDiscountDto.setDiscountAmount(BigDecimal.valueOf(10));
        adHocDiscountDto.setDiscountCouponCode("test ad-hoc coupon code");
        adHocDiscountDto.setDiscountGroupId(1L);
        adHocDiscountDto.setDiscountKeyerId(UUID.randomUUID());
        adHocDiscountDto.setDiscountName("test ad-hoc");
        return adHocDiscountDto;
    }
}
