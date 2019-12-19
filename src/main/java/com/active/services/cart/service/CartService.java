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
import com.active.services.cart.repository.CartItemFeeRepository;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.quote.CartPriceEngine;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.util.AuditorAwareUtil;
import com.active.services.cart.util.DataAccess;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemFeeRepository cartItemFeeRepository;

    private final CartPriceEngine cartPriceEngine;

    private final DataAccess dataAccess;

    private static final int UPDATE_SUCCESS = 1;

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
    public List<CartItem> createCartItems(Long cartId, UUID cartIdentifier, List<CartItem> items) {
        cartRepository.createCartItems(cartId, items);
        incrementVersion(cartIdentifier);
        return items;
    }

    @Transactional
    public List<CartItem> updateCartItems(UUID cartIdentifier, List<CartItem> items) {
        checkCartItem(cartIdentifier, items.stream().map(CartItem::getIdentifier).collect(Collectors.toList()));
        cartRepository.updateCartItems(items);
        incrementVersion(cartIdentifier);
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
        incrementVersion(cartId);
    }

    @Transactional
    public List<UUID> search(UUID ownerId) {
        List<UUID> uuidList = cartRepository.search(ownerId);
        return uuidList;
    }

    @Transactional
    public boolean finalizeCart(UUID cartId) {
        return cartRepository.finalizeCart(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
    }

    @Transactional
    public boolean incrementVersion(UUID cartId) {
        return cartRepository.incrementVersion(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
    }

    @Transactional
    public boolean incrementPriceVersion(UUID cartId) {
        return cartRepository.incrementPriceVersion(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
    }

    @Transactional
    public boolean acquireLock(UUID cartId) {
        return cartRepository.acquireLock(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
    }

    @Transactional
    public boolean releaseLock(UUID cartId) {
        return cartRepository.releaseLock(cartId, AuditorAwareUtil.getAuditor()) == UPDATE_SUCCESS;
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
            cartItemFeeRepository.deleteLastQuoteResult(item);
            item.getFees().stream().filter(Objects::nonNull).forEach(cartItemFee -> {
                cartItemFeeRepository.createCartItemFee(cartItemFee);
                CartItemCartItemFee cartItemCartItemFee = buildCartItemCartItemFee(item.getId(), cartItemFee.getId());
                cartItemFeeRepository.createCartItemCartItemFee(cartItemCartItemFee);
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
