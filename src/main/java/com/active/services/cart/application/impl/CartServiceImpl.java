package com.active.services.cart.application.impl;

import com.active.services.cart.application.CartService;
import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.infrastructure.repository.CartRepository;
import com.active.services.cart.infrastructure.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepo;
    private final ProductRepository productRepo;

    @Override
    public Cart createCart(Cart cart) {
        return cartRepo.save(cart);
    }
}