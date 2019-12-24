package com.active.services.cart.repository.mapper;

import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeeRelationship;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CartItemFeeMapper {
    void createCartItemFee(CartItemFee cartItemFee);

    void createCartItemCartItemFee(CartItemFeeRelationship cartItemFeeRelationship);

    void deleteCartItemFeeById(@Param("id") Long id);
}
