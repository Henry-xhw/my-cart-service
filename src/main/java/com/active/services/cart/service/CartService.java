package com.active.services.cart.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active.services.cart.common.OperationResultCode;
import com.active.services.cart.common.exception.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.CartRepository;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public void create(Cart cart) {
        cartRepository.createCart(cart);
    }

    public void delete(Long cartId) {
        cartRepository.deleteCart(cartId);
    }

    public Cart get(UUID cartId) {
        return cartRepository.getCart(cartId).orElseThrow(() -> new CartException(OperationResultCode.CART_NOT_EXIST.getCode(),
            OperationResultCode.CART_NOT_EXIST.getDescription() + " cart id: " + cartId));
    }

    public List<CartItem> upsertItems(UUID cartId, List<CartItem> items) {
        cartRepository.upsertItems(cartId, items);

        return items;
    }

    public void deleteCartItem(UUID cartItemId) {
        cartRepository.deleteCartItem(cartItemId);
    }

    public List<UUID> search(UUID ownerId) {
        return cartRepository.search(ownerId);
    }
}
