package com.active.services.cart.repository;

import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.CartItemFeeRelationship;
import com.active.services.cart.domain.CartItemFeesInCart;
import com.active.services.cart.repository.mapper.CartItemFeeMapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    public List<CartItemFeesInCart> getCartItemFeesByCartId(Long cartId) {
        return cartItemFeeMapper.getCartItemFeesByCartId(cartId);
    }
}
