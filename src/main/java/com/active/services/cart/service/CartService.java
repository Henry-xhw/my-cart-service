package com.active.services.cart.service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemCartItemFee;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.quote.CartPriceEngine;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.util.DataAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartPriceEngine cartPriceEngine;

    private final DataAccess dataAccess;

    @Transactional
    public void create(Cart cart) {
        cartRepository.createCart(cart);
    }

    @Transactional
    public void delete(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    @Transactional
    public Cart get(UUID cartId) {
        return cartRepository.getCart(cartId).orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND,
            " cart id does not exist: " + cartId));
    }

    @Transactional
    public List<CartItem> createCartItems(Long cartId, List<CartItem> items) {
        cartRepository.createCartItems(cartId, items);
        return items;
    }

    @Transactional
    public List<CartItem> updateCartItems(UUID cartIdentifier, List<CartItem> items) {
        checkCartItem(cartIdentifier, items.stream().map(CartItem::getIdentifier).collect(Collectors.toList()));
        cartRepository.updateCartItems(items);
        return items;
    }

    private void checkCartItem(UUID cartId, List<UUID> itemIds) {
        Cart cart = get(cartId);

        for (UUID itemId : itemIds) {
            if (!cart.findCartItem(itemId).isPresent()) {
                throw new CartException(ErrorCode.CART_NOT_FOUND, "cart item does not exist: " + itemId);
            }
        }
    }

    @Transactional
    public void deleteCartItem(UUID cartId, UUID cartItemId) {
        checkCartItem(cartId, Arrays.asList(cartItemId));

        cartRepository.deleteCartItem(cartItemId);
    }

    @Transactional
    public List<UUID> search(UUID ownerId) {
        List<UUID> uuidList = cartRepository.search(ownerId);
        return uuidList;
    }

    public Cart quote(UUID cartId) {
        Cart cart = get(cartId);
        cartPriceEngine.quote(new CartQuoteContext(cart));

        // Manual control the tx
        dataAccess.doInTx(() -> {
            saveQuoteResult(cart);
        });
        return cart;
    }

    private void saveQuoteResult(Cart cart) {
        cart.getItems().stream().filter(Objects::nonNull).forEach(item -> {
            cartRepository.deleteLastQuoteResult(item);
            item.getFees().stream().filter(Objects::nonNull).forEach(cartItemFee -> {
                cartRepository.createCartItemFee(cartItemFee);
                CartItemCartItemFee cartItemCartItemFee = buildCartItemCartItemFee(item.getId(), cartItemFee.getId());
                cartRepository.createCartItemCartItemFee(cartItemCartItemFee);
            });
        });
    }

    private CartItemCartItemFee buildCartItemCartItemFee(Long cartItemId, Long cartItemFeeId) {
        CartItemCartItemFee cartItemCartItemFee = new CartItemCartItemFee();
        cartItemCartItemFee.setCartItemFeeId(cartItemFeeId);
        cartItemCartItemFee.setCartItemId(cartItemId);
        cartItemCartItemFee.setIdentifier(UUID.randomUUID());
        return cartItemCartItemFee;
    }
}
