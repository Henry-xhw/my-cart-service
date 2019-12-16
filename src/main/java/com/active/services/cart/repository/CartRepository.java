package com.active.services.cart.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.active.services.cart.domain.CartItemCartItemFee;
import com.active.services.cart.domain.CartItemFee;
import org.springframework.stereotype.Repository;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.repository.mapper.CartMapper;

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
        cartMapper.deleteCartItemByCartId(cartId);
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

    public void saveQuoteResult(Cart cart) {
        Optional.ofNullable(cart.getItems()).ifPresent(cartItems ->
            cartItems.forEach(item -> Optional.ofNullable(item.getFees()).
                ifPresent(cartItemFees -> cartItemFees.forEach(cartItemFee -> {
                    deleteLastQuoteResult(item);
                    saveQuoteResult(item, cartItemFee);
                }))));
    }

    private void saveQuoteResult(CartItem item, CartItemFee cartItemFee) {
        Long cartItemFeeId = cartMapper.createCartItemFee(cartItemFee);
        CartItemCartItemFee cartItemCartItemFee = new CartItemCartItemFee();
        cartItemCartItemFee.setIdentifier(UUID.randomUUID());
        cartItemCartItemFee.setCartItemFeeId(cartItemFeeId);
        cartItemCartItemFee.setCartItemId(item.getId());
        cartItemCartItemFee.setIdentifier(UUID.randomUUID());
        cartMapper.createCartItemCartItemFee(cartItemCartItemFee);
    }

    private void deleteLastQuoteResult(CartItem item) {
        cartMapper.deleteCartItemFeeById(item.getId());
        cartMapper.deleteCartItemCartItemFeeBycartItemId(item.getId());
    }
}
