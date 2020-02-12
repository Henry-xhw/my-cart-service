package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeeRelationship;
import com.active.services.cart.domain.CartItemFeesInCart;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartItemFeeMapper {
    void createCartItemFee(CartItemFee cartItemFee);

    void createCartItemCartItemFee(CartItemFeeRelationship cartItemFeeRelationship);

    void deleteCartItemFeeById(@Param("id") Long id);

    List<CartItemFeesInCart> getCartItemFeesByCartId(@Param("id") Long cartId);
}
