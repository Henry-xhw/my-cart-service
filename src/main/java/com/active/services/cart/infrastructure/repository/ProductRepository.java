package com.active.services.cart.infrastructure.repository;

import com.active.services.AuditableObject;
import com.active.services.Availability;
import com.active.services.ContextWrapper;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.Discount;
import com.active.services.product.Product;
import com.active.services.product.api.v1.soap.ProductServiceSOAPEndPoint;
import com.active.services.product.discount.multi.MultiDiscount;
import com.active.services.product.discount.multi.MultiDiscountType;
import com.google.common.collect.MoreCollectors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@RequiredArgsConstructor
@Repository
public class ProductRepository {
    private final ProductServiceSOAPEndPoint prdSvc;

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

    /**
     *
     * TODO: we might need a new API from product service for this, because the current API is mainly for display
     * purpose without filtering and segregation, however it's needed from following, see MultiDiscountLinesMapper.hasMdOfSameType
     *      * Return true and log a warning if the order line already has a
     *      * MultiDiscount of the same type. This shouldn't happen since the db query
     *      * for "effective" MD's partitions them by type. Thus, within each type only
     *      * one MD should be considered the effective one.
     */
    public List<MultiDiscount> findEffectiveMultiDiscountsByProductId(Long productId, LocalDateTime priceDate) {
        Map<MultiDiscountType, List<MultiDiscount>> type2Mds = emptyIfNull(prdSvc.findMultiDiscountsByProductId(ContextWrapper.get(), productId)).stream()
                .filter(md -> md.getAvailability() == Availability.ONLINE)
                .filter(md -> (md.getStartDate() == null || md.getStartDate().before(new DateTime(priceDate))) &&
                        (md.getEndDate() == null || md.getEndDate().after(new DateTime(priceDate))))
                .collect(groupingBy(MultiDiscount::getDiscountType));

        List<MultiDiscount> mds = new ArrayList<>(2);
        for (Map.Entry<MultiDiscountType, List<MultiDiscount>> e : type2Mds.entrySet()) {
            MultiDiscount md = e.getValue().stream()
                    .sorted(Comparator.comparing(AuditableObject::getCreatedDate))
                    .limit(1)
                    .collect(MoreCollectors.onlyElement());
            mds.add(md);
        }

        return mds.stream()
                .filter(md -> CollectionUtils.isNotEmpty(md.getThresholdSettings()))
                .filter(md -> CollectionUtils.isNotEmpty(md.getTiers()))
                .collect(Collectors.toList());
    }
}
