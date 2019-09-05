package com.active.services.cart.application.impl;

import org.springframework.stereotype.Service;

import com.active.services.cart.application.CartService;
import com.active.services.cart.application.RuleEngine;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.cart.model.CartDto;
import com.active.services.cart.model.CreateCartResp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final RuleEngine ruleEngine;
    private final ProductRepository productRepo;

    @Override
    public CreateCartResp createCart(CartDto cart) {
        return null;
    }
}