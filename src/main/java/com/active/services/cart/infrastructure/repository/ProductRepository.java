package com.active.services.cart.infrastructure.repository;

import com.active.services.cart.domain.rule.Rule;
import com.active.services.product.api.v1.soap.ProductServiceSOAPEndPoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final ProductServiceSOAPEndPoint prdSvc;

    public List<Rule> findProductFeeRulesByProductId(Long productId) {
        return null;
    }
}
