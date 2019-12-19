package com.active.services.cart.repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import com.active.services.cart.domain.CartItemCartItemFee;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.repository.mapper.CartItemFeeMapper;
import org.springframework.stereotype.Repository;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.mapper.CartMapper;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CartRepository {

    private final CartMapper cartMapper;

    private final CartItemFeeMapper cartItemFeeMapper;

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


    public void createCartItemFee(CartItemFee cartItemFee) {
        cartItemFeeMapper.createCartItemFee(cartItemFee);
    }

    public void createCartItemCartItemFee(CartItemCartItemFee cartItemCartItemFee) {
        cartItemFeeMapper.createCartItemCartItemFee(cartItemCartItemFee);
    }

    public void deleteLastQuoteResult(CartItem item) {
        cartItemFeeMapper.deleteCartItemFeeById(cartItemFeeMapper.getCartItemFeeIdByCartItemId(item.getId()));
        cartItemFeeMapper.deleteCartItemCartItemFeeByCartItemId(item.getId());
    }
}
