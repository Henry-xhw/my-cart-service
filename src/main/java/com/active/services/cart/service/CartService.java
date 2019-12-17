package com.active.services.cart.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.active.services.cart.common.CartException;
import com.active.services.cart.model.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.quote.CartPriceEngine;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.util.DataAccess;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartPriceEngine cartPriceEngine;

    @Autowired
    private DataAccess dataAccess;

    public void create(Cart cart) {
        cartRepository.createCart(cart);
    }

    public void delete(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    public Cart get(UUID cartId) {
        return cartRepository.getCart(cartId).orElseThrow(() -> new CartException(ErrorCode.CART_NOT_FOUND,
            " cart id does not exist: " + cartId));
    }

    public List<CartItem> createCartItems(Long cartId, List<CartItem> items) {
        cartRepository.createCartItems(cartId, items);
        return items;
    }

    public List<CartItem> updateCartItems(UUID cartIdentifier, List<CartItem> items) {
        checkCartItem(cartIdentifier, items.stream().map(CartItem::getIdentifier).collect(Collectors.toList()));
        cartRepository.updateCartItems(items);
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

    public void deleteCartItem(UUID cartId, UUID cartItemId) {
        checkCartItem(cartId, Arrays.asList(cartItemId));

        cartRepository.deleteCartItem(cartItemId);
    }

    public List<UUID> search(UUID ownerId) {
        List<UUID> uuidList = cartRepository.search(ownerId);
        return uuidList;
    }


    public Cart quote(UUID cartId) {
        Cart cart = get(cartId);

        cartPriceEngine.quote(new CartQuoteContext(cart));

        // Manual control the tx
        dataAccess.doInTx(() -> {
            cartRepository.saveQuoteResult(cart);
        });

        return cart;
    }
}
