package com.active.services.cart.service;

import com.active.services.ContextWrapper;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.AdHocDiscount;
import com.active.services.cart.domain.BaseTree;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeeRelationship;
import com.active.services.cart.domain.CartItemFeesInCart;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.cart.repository.AdHocDiscountRepository;
import com.active.services.cart.repository.CartItemFeeRepository;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.repository.DiscountRepository;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.cart.service.checkout.CheckoutProcessor;
import com.active.services.cart.service.quote.CartPriceEngine;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.validator.CartItemsCurrencyFormatValidator;
import com.active.services.cart.service.validator.CreateCartItemsValidator;
import com.active.services.cart.util.DataAccess;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.SetUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartService {
    private final CartRepository cartRepository;

    private final CartItemFeeRepository cartItemFeeRepository;

    private final CartPriceEngine cartPriceEngine;

    private final DiscountRepository discountRepository;

    private final DataAccess dataAccess;

    private final AdHocDiscountRepository adHocDiscountRepository;

    @Transactional
    public void create(Cart cart) {
        cart.setCouponCodes(distinctCouponCodes(cart.getCouponCodes()));
        cartRepository.createCart(cart);
    }

    @Transactional
    public void update(Cart cart) {
        cart.setId(getCartByUuid(cart.getIdentifier()).getId());
        cart.setCouponCodes(distinctCouponCodes(cart.getCouponCodes()));
        cartRepository.updateCart(cart);
        incrementVersion(cart.getIdentifier());
    }

    @Transactional
    public void delete(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    public Cart getCartByUuid(UUID cartId) {
        Cart cart = cartRepository.getCart(cartId).orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND,
                " cart id does not exist: {0}", cartId));
        return cart;
    }

    @Transactional
    public long createCartItem(Long cartId, BaseTree<CartItem> cartItem) {
        return cartRepository.createCartItem(cartId, cartItem);
    }

    @Transactional
    public List<CartItem> updateCartItems(UUID cartIdentifier, List<CartItem> items) {
        Cart cart = getCartByUuid(cartIdentifier);

        Map<UUID, CartItem> cartItemByIdentifier = cart.getFlattenCartItems()
                .stream().collect(toMap(CartItem::getIdentifier, Function.identity()));
        List<Long> cartItemIds = new ArrayList<>();
        items.forEach(item -> {
            CartItem foundCartItem = cartItemByIdentifier.get(item.getIdentifier());
            if (foundCartItem == null) {
                throw new CartException(ErrorCode.VALIDATION_ERROR,
                        "cart item id: {0} is not belong cart id: {1}", item.getIdentifier(), cart.getIdentifier());
            }

            cartItemIds.add(foundCartItem.getId());
        });
        new CartItemsCurrencyFormatValidator(cart.getCurrencyCode(), items).validate();
        deleteAdHocDiscountByCartItemId(cartItemIds);

        items.forEach(item -> {
            item.setCouponCodes(distinctCouponCodes(item.getCouponCodes()));
            createAdHocDiscounts(cartItemByIdentifier.get(item.getIdentifier()).getId(), item.getAdHocDiscounts());
        });
        cartRepository.updateCartItems(items);
        incrementVersion(cartIdentifier);

        return items;
    }

    @Transactional
    public void deleteCartItem(Cart cart, UUID cartItemUuid) {
        CartItem cartItem = cart.findCartItem(cartItemUuid)
                .orElseThrow(() -> new CartException(ErrorCode.VALIDATION_ERROR,
                        "cart item id: {0} is not belong cart id: {1}", cartItemUuid, cart.getIdentifier()));
        List<UUID> idsToDelete = Cart.flattenCartItems(Arrays.asList(cartItem)).stream()
                .map(CartItem::getIdentifier).collect(Collectors.toList());

        cartRepository.batchDeleteCartItems(idsToDelete);
        incrementVersion(cart.getIdentifier());
    }

    public List<UUID> search(UUID ownerId) {
        return cartRepository.search(ownerId);
    }

    public void insertCartItems(UUID cartIdentifier, List<CartItem> cartItems) {
        Cart cart = getCartByUuid(cartIdentifier);
        getCartItemsValidator(cart, cartItems).validate();
        dataAccess.doInTx(() -> doInsertCartItems(cart, cartItems, null));
    }

    private void doInsertCartItems(Cart cart, List<CartItem> cartItems, Long requestParentId) {
        Long cartId = cart.getId();

        for (CartItem it : cartItems) {
            Long parentId = requestParentId;
            if (it.getIdentifier() != null) {
                parentId = cart.findCartItem(it.getIdentifier()).get().getId();
            } else {
                it.setIdentifier(UUID.randomUUID());
                it.setParentId(parentId);
                it.setCouponCodes(distinctCouponCodes(it.getCouponCodes()));
                parentId = createCartItem(cartId, it);
                createAdHocDiscounts(parentId, it.getAdHocDiscounts());
            }
            List<CartItem> subItems = it.getSubItems();
            if (subItems.size() > 0) {
                doInsertCartItems(cart, subItems, parentId);
            }
        }
        incrementVersion(cart.getIdentifier());
    }

    @Lookup
    public CreateCartItemsValidator getCartItemsValidator(Cart cart, List<CartItem> cartItems) {
        return null;
    }

    public Cart quote(CartQuoteContext quoteContext) {
        Cart cart = quoteContext.getCart();
        try {
            CartQuoteContext.set(quoteContext);
            cartPriceEngine.quote(quoteContext);
            // Manual control the tx
            dataAccess.doInTx(() -> {
                saveQuoteResult(quoteContext);
                incrementPriceVersion(cart.getIdentifier());
            });
        } finally {
            CartQuoteContext.destroy();
        }

        return cart;
    }

    public List<CheckoutResult> checkout(UUID cartId, CheckoutContext context) {
        Cart cart = getCartWithFullPriceByUuid(cartId);
        context.setCart(cart);

        List<CartItemFee> cartItemFees = cart.getFlattenCartItems().stream()
                .filter(item -> Objects.nonNull(item.getFees())).map(CartItem::getFlattenCartItemFees)
                .flatMap(List::stream).collect(Collectors.toList());
        context.setFlattenCartItemFees(cartItemFees);

        getCheckoutProcessor(context).process();

        return context.getCheckoutResults();
    }

    @Lookup
    public CheckoutProcessor getCheckoutProcessor(CheckoutContext context) {
        return null;
    }

    private void incrementVersion(UUID cartId) {
        cartRepository.incrementVersion(cartId, ContextWrapper.get().getActorId());
    }

    private void incrementPriceVersion(UUID cartId) {
        cartRepository.incrementPriceVersion(cartId, ContextWrapper.get().getActorId());
    }

    private void saveQuoteResult(CartQuoteContext cartQuoteContext) {
        Cart cart = cartQuoteContext.getCart();
        cartItemFeeRepository.deleteLastQuoteResult(cart.getId());
        discountRepository.deletePreviousDiscountByCartId(cart.getId());
        cartQuoteContext.getAppliedDiscounts().forEach(discountRepository::createDiscount);
        cartQuoteContext.getFlattenCartItems().stream().filter(Objects::nonNull).forEach(item -> {
            item.getFees().stream().filter(Objects::nonNull).forEach(cartItemFee -> {
                createCartItemFeeAndRelationship(cartItemFee, item.getId());
            });
        });
        cartRepository.updateCartItems(cart.getItems());
    }

    private void createCartItemFeeAndRelationship(CartItemFee cartItemFee, Long itemId) {
        cartItemFeeRepository.createCartItemFee(cartItemFee);
        cartItemFeeRepository.createCartItemCartItemFee(
                CartItemFeeRelationship.buildCartItemCartItemFee(itemId, cartItemFee.getId()));
        createSubFeeAndRelationship(cartItemFee, itemId);
    }

    private void createSubFeeAndRelationship(CartItemFee itemFee, Long itemId) {
        emptyIfNull(itemFee.getSubItems()).stream()
                .filter(Objects::nonNull)
                .forEach(itemFee1 -> {
                    itemFee1.setParentId(itemFee.getId());
                    createCartItemFeeAndRelationship(itemFee1, itemId);
                }
        );
    }

    private Cart getCartWithFullPriceByUuid(UUID cartId) {
        Cart cart = getCartByUuid(cartId);
        buildCartItemFeeTree(cart);
        return cart;
    }

    private void buildCartItemFeeTree(Cart cart) {
        List<CartItemFeesInCart> cartItemFees = cartItemFeeRepository.getCartItemFeesByCartId(cart.getId());
        cart.getFlattenCartItems().forEach(cartItem -> {
            List<CartItemFee> collect =
                    cartItemFees.stream().filter(itemFee -> itemFee.getCartItemId() == cartItem.getId() &&
                            Objects.nonNull(itemFee.getId())).collect(Collectors.toList());
            cartItem.setUnflattenItemFees(collect);
        });
    }

    private Set<String> distinctCouponCodes(Set<String> couponCodes) {
        return SetUtils.emptyIfNull(couponCodes).stream().filter(StringUtils::isNotBlank).map(String::toUpperCase)
                .collect(Collectors.toSet());
    }

    @Transactional
    public void createAdHocDiscounts(Long cartItemId, List<AdHocDiscount> adHocDiscounts) {
        if (CollectionUtils.isNotEmpty(adHocDiscounts)) {
            adHocDiscounts.stream().forEach(adHocDiscount -> {
                adHocDiscount.setIdentifier(UUID.randomUUID());
                adHocDiscount.setCartItemId(cartItemId);
            });
            adHocDiscountRepository.createAdHocDiscounts(adHocDiscounts);
        }
    }

    @Transactional
    public void deleteAdHocDiscountByCartItemId(List<Long> cartItemIds) {
        adHocDiscountRepository.deleteAdHocDiscountByCartItemId(cartItemIds);
    }
}