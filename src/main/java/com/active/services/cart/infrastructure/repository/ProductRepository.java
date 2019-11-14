package com.active.services.cart.infrastructure.repository;

import com.active.services.ContextWrapper;
import com.active.services.cart.domain.rule.Rule;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.Discount;
import com.active.services.product.Product;
import com.active.services.product.api.v1.soap.ProductServiceSOAPEndPoint;
import com.active.services.product.discount.multi.MultiDiscount;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

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

    public List<Discount> findDiscountByProductIdAndCode(Long productId, String coupon) {
        return null;
    }

    public boolean isDiscountLimitReached(Discount discount, Integer quantity, Long ignoreId) {
        return false;
    }

    public List<MultiDiscount> findMultiDiscountsByProductId(Long productId) {
        return emptyIfNull(prdSvc.findMultiDiscountsByProductId(ContextWrapper.get(), productId)).stream()
                .filter(md -> CollectionUtils.isNotEmpty(md.getThresholdSettings()))
                .filter(md -> CollectionUtils.isNotEmpty(md.getTiers()))
                .collect(Collectors.toList());
    }
}
