package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.CartItemCartItemFee;
import com.active.services.cart.domain.CartItemFee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartItemFeeMapper {
    void createCartItemFee(CartItemFee cartItemFee);

    void createCartItemCartItemFee(CartItemCartItemFee cartItemCartItemFee);

    Long getCartItemFeeIdByCartItemId(@Param("cartItemId") Long cartItemId);

    void deleteCartItemFeeById(@Param("id") Long id);

    void deleteCartItemCartItemFeeByCartItemId(@Param("cartItemId") Long cartItemId);
}
