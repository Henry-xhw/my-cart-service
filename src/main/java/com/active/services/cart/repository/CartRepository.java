package com.active.services.cart.repository;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.mapper.CartMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final CartMapper cartMapper;

    public void createCart(Cart cart) {
        cartMapper.createCart(cart);
    }

    public void deleteCart(Long cartId) {
        cartMapper.deleteCart(cartId);
    }

    public Optional<Cart> getCart(UUID cartId) {
        return cartMapper.getCart(cartId);
    }

    public void createCartItems(Long cartId, List<CartItem> items) {
        items.forEach(item -> cartMapper.createCartItem(cartId, item));
    }

    public void updateCartItems(List<CartItem> items) {
        items.forEach(item -> cartMapper.updateCartItem(item));
    }

    public void deleteCartItem(UUID cartItemId) {
        cartMapper.deleteCartItem(cartItemId);
    }

    public List<UUID> search(UUID ownerId) {
        return cartMapper.search(ownerId);
    }

    public long createCartItem(Long cartId, CartItem cartItem) {
        cartMapper.createCartItem(cartId, cartItem);
        return cartItem.getId();
    }
}
