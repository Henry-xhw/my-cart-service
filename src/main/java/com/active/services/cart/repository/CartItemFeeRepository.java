package com.active.services.cart.repository;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemCartItemFee;
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

    public void createCartItemCartItemFee(CartItemCartItemFee cartItemCartItemFee) {
        cartItemFeeMapper.createCartItemCartItemFee(cartItemCartItemFee);
    }

    public void deleteLastQuoteResult(CartItem item) {
        Long cartItemFeeIdByCartItemId = cartItemFeeMapper.getCartItemFeeIdByCartItemId(item.getId());
        cartItemFeeMapper.deleteCartItemCartItemFeeByCartItemId(item.getId());
        cartItemFeeMapper.deleteCartItemFeeById(cartItemFeeIdByCartItemId);
    }
}
