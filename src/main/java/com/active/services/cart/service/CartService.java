package com.active.services.cart.service;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public Optional<Cart> get(UUID cartId) {
        return cartRepository.getCart(cartId);
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
