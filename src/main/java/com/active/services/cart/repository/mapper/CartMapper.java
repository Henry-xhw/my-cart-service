package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemCartItemFee;
import com.active.services.cart.domain.CartItemFee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface CartMapper {
    void createCart(Cart cart);

    void deleteCart(Long cartId);

    Optional<Cart> getCart(@Param("cartId") UUID cartId);

    void updateCartItem(@Param("item") CartItem item);

    void createCartItem(@Param("cartId") Long cartId, @Param("item") CartItem item);

    void deleteCartItem(UUID cartItemId);

    void deleteCartItemByCartId(Long cartId);

    List<UUID> search(@Param("ownerId") UUID ownerId);

    void createCartItemFee(CartItemFee cartItemFee);

    void createCartItemCartItemFee(CartItemCartItemFee cartItemCartItemFee);

    Long getCartItemFeeIdByCartItemId(@Param("cartItemId") Long cartItemId);

    void deleteCartItemFeeById(@Param("id") Long id);

    void deleteCartItemCartItemFeeBycartItemId(@Param("cartItemId") Long cartItemId);
}
