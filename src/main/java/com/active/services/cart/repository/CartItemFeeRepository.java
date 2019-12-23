package com.active.services.cart.repository;

import com.active.services.cart.domain.CartItemFeeRelationship;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.repository.mapper.CartItemFeeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartItemFeeRepository {

    private final CartItemFeeMapper cartItemFeeMapper;

    public void createCartItemFee(CartItemFee cartItemFee) {
        cartItemFeeMapper.createCartItemFee(cartItemFee);
    }

    public void createCartItemCartItemFee(CartItemFeeRelationship cartItemFeeRelationship) {
        cartItemFeeMapper.createCartItemCartItemFee(cartItemFeeRelationship);
    }

    public void deleteLastQuoteResult(Long id) {
        cartItemFeeMapper.deleteCartItemFeeById(id);
    }
}
