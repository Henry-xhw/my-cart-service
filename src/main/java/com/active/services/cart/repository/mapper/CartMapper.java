package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.UUID;

@Mapper
public interface CartMapper {
    void createCart(Cart cart);

    void deleteCart(UUID cartId);

    Cart getCart(@Param("cartId") UUID cartId);

    void updateCartItem(@Param("item") CartItem item);

    void createCartItem(@Param("cartId") Long cartId, @Param("item") CartItem item);

    void deleteCartItem(UUID cartItemId);

    List<UUID> search(@Param("ownerId") UUID ownerId);
}
