package com.active.services.cart.repository.mapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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

    int finalizeCart(@Param("identifier") UUID cartId,@Param("modifiedBy") String modifiedBy);

    int incrementVersion(@Param("identifier") UUID cartId, @Param("modifiedBy") String modifiedBy);

    int incrementPriceVersion(@Param("identifier") UUID cartId, @Param("modifiedBy") String modifiedBy);

    int acquireLock(@Param("identifier") UUID cartId, @Param("modifiedBy") String modifiedBy);

    int releaseLock(@Param("identifier") UUID cartId, @Param("modifiedBy") String modifiedBy);

}
