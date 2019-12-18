package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.Instant;
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

    int finalizedCart(@Param("identifier") UUID cartId,
                        @Param("modifiedBy") String modifiedBy,
                        @Param("modifiedDt") Instant modifiedDt);

    int incrementVersion(@Param("identifier") UUID cartId,
        @Param("modifiedBy") String modifiedBy,
        @Param("modifiedDt") Instant modifiedDt);

    int incrementPriceVersion(@Param("identifier") UUID cartId,
        @Param("modifiedBy") String modifiedBy,
        @Param("modifiedDt") Instant modifiedDt);

    int getLock(@Param("identifier") UUID cartId,
        @Param("modifiedBy") String modifiedBy,
        @Param("modifiedDt") Instant modifiedDt);

    int releaseLock(@Param("identifier") UUID cartId,
        @Param("modifiedBy") String modifiedBy,
        @Param("modifiedDt") Instant modifiedDt);

}
