package com.active.services.cart.repository;

import com.active.services.cart.repository.mapper.CartItemFeeMapper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class CartItemFeeRepositoryTest {

    @Mock
    private CartItemFeeMapper cartItemFeeMapper;

    @InjectMocks
    private CartItemFeeRepository cartItemFeeRepository;

    @Test
    public void createCartItemFee() {
        doNothing().when(cartItemFeeMapper).createCartItemFee(any());
        cartItemFeeRepository.createCartItemFee(any());
        verify(cartItemFeeMapper).createCartItemFee(any());
    }

    @Test
    public void createCartItemCartItemFee() {
        doNothing().when(cartItemFeeMapper).createCartItemCartItemFee(any());
        cartItemFeeRepository.createCartItemCartItemFee(any());
        verify(cartItemFeeMapper).createCartItemCartItemFee(any());
    }

    @Test
    public void deleteLastQuoteResult() {
        doNothing().when(cartItemFeeMapper).deleteCartItemFeeById(any());
        cartItemFeeRepository.deleteLastQuoteResult(any());
        verify(cartItemFeeMapper, times(1)).deleteCartItemFeeById(any());
    }

    @Test
    public void getCartItemFeesByCartId() {
        when(cartItemFeeMapper.getCartItemFeesByCartId(any())).thenReturn(null);
        cartItemFeeRepository.getCartItemFeesByCartId(any());
        verify(cartItemFeeMapper, times(1)).getCartItemFeesByCartId(any());
    }
}
