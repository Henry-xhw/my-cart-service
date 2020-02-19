package com.active.services.cart.repository.mapper;

import com.active.services.cart.common.Event;
import com.active.services.cart.domain.BaseTree;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mapper
public interface CartMapper {
    void createCart(Cart cart);

    void updateCart(Cart cart);

    void deleteCart(Long cartId);

    Optional<Cart> getCart(@Param("cartId") UUID cartId);

    void updateCartItem(@Param("item") CartItem item);

    void batchDeleteCartItems(List<UUID> uuidList);

    void createCartItem(@Param("cartId") Long cartId, @Param("item") BaseTree<CartItem> item);

    int incrementPriceVersion(@Param("identifier") UUID cartId, @Param("modifiedBy") String modifiedBy);

    int incrementVersion(@Param("identifier") UUID cartId, @Param("modifiedBy") String modifiedBy);

    void deleteCartItem(Long cartItemId);

    List<UUID> search(@Param("ownerId") UUID ownerId);

    Optional<Long> getCartItemIdByCartItemUuid(@Param("cartItemId") UUID cartItemId);

    int finalizeCart(@Param("identifier") UUID cartId, @Param("modifiedBy") String modifiedBy);

    int acquireLock(@Param("identifier") UUID cartId, @Param("modifiedBy") String modifiedBy);

    int releaseLock(@Param("identifier") UUID cartId, @Param("modifiedBy") String modifiedBy);

    void updateCartReservationGroupId(@Param("identifier") UUID cartId,
                                      @Param("reservationId") UUID reservationId,
                                      @Param("modifiedBy") String modifiedBy);

    void createEvents(List<Event> events);
}
