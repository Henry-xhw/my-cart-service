package com.active.services.cart.application.impl;

import com.active.services.cart.application.CartService;
import com.active.services.cart.application.RuleEngine;
import com.active.services.cart.domain.rule.Rule;
import com.active.services.cart.domain.rule.product.ProductFact;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.cart.model.CartItem;
import com.active.services.cart.model.CartItemFacts;
import com.active.services.cart.model.CreateCartRequest;
import com.active.services.cart.model.CreateCartResp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final RuleEngine ruleEngine;
    private final ProductRepository productRepo;

    @Override
    public CreateCartResp createCart(CreateCartRequest cart) {
        for (CartItem item : cart.getCartItems()) {
            List<Rule> prodRules = productRepo.findProductFeeRulesByProductId(item.getProductId());
            CartItemFacts cif = item.getCartItemFacts();
            ProductFact fact = new ProductFact(cif.getKvFactPairs());
            ruleEngine.runRules(prodRules, fact);
            LOG.info("rule execution result: {}", fact.getResult());
            item.setPrice(fact.getResult().getAmount());
        }
        return null;
    }
}