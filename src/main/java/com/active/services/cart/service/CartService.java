package com.active.services.cart.service;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.active.services.cart.util.AuditorAwareUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.repository.CartRepository;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Transactional
    public void create(Cart cart) {
        cartRepository.createCart(cart);
    }

    @Transactional
    public void delete(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    private static final int UPDATA_SUCCESS = 1;

    @Transactional
    public Cart get(UUID cartId) {
        return cartRepository.getCart(cartId)
            .orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND, " cart id does not exist: " + cartId));
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
            if (!cart.getCartItem(itemId).isPresent()) {
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
    public void finalizedCart(UUID cartId) {
        if (cartRepository.finalizedCart(cartId, AuditorAwareUtil.getAuditor().orElse("system"),
            Instant.now()) != UPDATA_SUCCESS) {
            throw new CartException(ErrorCode.INTERNAL_ERROR, "finalized cart fail: " + cartId);
        }
    }

    @Transactional
    public void incrementVersion(UUID cartId) {
        if (cartRepository.incrementVersion(cartId, AuditorAwareUtil.getAuditor().orElse("system"),
            Instant.now()) != UPDATA_SUCCESS) {
            throw new CartException(ErrorCode.INTERNAL_ERROR, "increment version fail: " + cartId);
        }
    }

    @Transactional
    public void incrementPriceVersion(UUID cartId) {
        if (cartRepository.incrementPriceVersion(cartId, AuditorAwareUtil.getAuditor().orElse("system"),
            Instant.now()) != UPDATA_SUCCESS) {
            throw new CartException(ErrorCode.INTERNAL_ERROR, "increment price version fail: " + cartId);
        }
    }

    @Transactional
    public void getLock(UUID cartId) {
        if (cartRepository.getLock(cartId, AuditorAwareUtil.getAuditor().orElse("system"),
            Instant.now()) != UPDATA_SUCCESS) {
            throw new CartException(ErrorCode.INTERNAL_ERROR, "get lock fail: " + cartId);
        }
    }

    @Transactional
    public void releaseLock(UUID cartId) {
        if (cartRepository.releaseLock(cartId, AuditorAwareUtil.getAuditor().orElse("system"),
            Instant.now()) != UPDATA_SUCCESS) {
            throw new CartException(ErrorCode.INTERNAL_ERROR, "release lock fail: " + cartId);
        }
    }
}
