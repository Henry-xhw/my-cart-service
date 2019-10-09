package com.active.services.cart.infrastructure.repository;

import com.active.services.cart.domain.rule.Rule;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.Product;
import com.active.services.product.api.v1.soap.ProductServiceSOAPEndPoint;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final ProductServiceSOAPEndPoint prdSvc;

    public List<Rule> findProductFeeRulesByProductId(Long productId) {
        return null;
    }

    public Optional<Product> getProduct(Long productId) {
        return Optional.empty();
    }

    public Map<Long, List<MembershipDiscountsHistory>> findMembershipDiscounts(List<Long> productIds) {
        return null;
    }

    public List<ProductMembership> findProductMemberships(List<Long> productIds) {
        return null;
    }
}
