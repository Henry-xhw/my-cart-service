package com.active.services.cart.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
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

    @Transactional
    public Cart get(UUID cartId) {
        return cartRepository.getCart(cartId).orElseThrow(() -> new CartException(HttpStatus.NOT_FOUND,
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
            if (!cart.getCartItem(itemId).isPresent()) {
                throw new CartException(HttpStatus.NOT_FOUND, "cart item does not exist: " + itemId);
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
}
