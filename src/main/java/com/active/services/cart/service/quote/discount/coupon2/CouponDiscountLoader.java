package com.active.services.cart.service.quote.discount.coupon2;

import com.active.platform.concurrent.Task;
import com.active.platform.concurrent.TaskRunner;
import com.active.services.Context;
import com.active.services.ContextWrapper;
import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountMapper;
import com.active.services.product.Discount;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;
import com.active.services.product.nextgen.v1.req.GetDiscountUsageReq;
import com.active.services.product.nextgen.v1.rsp.GetDiscountUsageRsp;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 *
 * Batch load product service available product/coupon mapping to improve performance.
 *
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CouponDiscountLoader {

    private final CartQuoteContext context;

    private final List<CartItem> cartItems;

    @Autowired
    private SOAPClient soapClient;

    @Autowired
    private TaskRunner taskRunner;

    @Autowired
    private ProductService productService;

    /**
     * Get product service available discounts builder for future filter.
     *
     * @return CartItemDiscounts
     */
    public List<CartItemDiscounts> loadDiscounts() {
        // Filter qualified cart items
        // Group cart item by productId + couponCodes.
        Map<FindLatestDiscountsByProductIdAndCouponCodesKey, List<CartItem>> couponTargetsByKey =
                cartItems.stream().filter(cartItem -> cartItemCouponKey(context, cartItem).isPresent())
                        .collect(groupingBy(cartItem -> cartItemCouponKey(context, cartItem).get()));

        if (MapUtils.isEmpty(couponTargetsByKey)) {
            return new ArrayList<>();
        }

        List<CartItemDiscounts> results = loadCartItemDiscounts(couponTargetsByKey);
        if (CollectionUtils.isEmpty(results)) {
            return new ArrayList<>();
        }

        List<Long> discountIds = results.stream().map(cid -> cid.getAllDiscountIds())
                .flatMap(Collection::stream).distinct().collect(Collectors.toList());
        List<Long> qualifiedIds = filterQualifiedDiscountIds(discountIds);

        // Sort CartItemDiscounts by cartItem net price in reverse order.
        //
        // For example, given the following 3 cart items and percent MOST_EXPENSIVE discount 20% <br>
        // cart item 1 = 100 <br>
        // cart item 2 = 200 <br>
        // cart item 3 = 80 <br>
        // the discount will only apply for cart item 2. cart item discount fee amount = 40.
        return results.stream()
                .map(cid -> cid.filterDiscounts(qualifiedIds))
                .filter(cartItemDiscounts -> CollectionUtils.isNotEmpty(cartItemDiscounts.getDiscounts()))
                .sorted(Comparator.comparing(CartItemDiscounts::getTotalNetPrice).reversed())
                .collect(Collectors.toList());
    }


    @NotNull
    private List<CartItemDiscounts> loadCartItemDiscounts(
            Map<FindLatestDiscountsByProductIdAndCouponCodesKey, List<CartItem>> couponTargetsByKey) {
        Context soapContext = ContextWrapper.get();
        List<Task<List<CartItemDiscounts>>> tasks = new ArrayList<>();
        couponTargetsByKey.forEach((key, items) -> {
            // Build task for each product + couponCode combination.
            Task<List<CartItemDiscounts>> task = () -> {
                List<Discount> discounts = soapClient.getProductOMSEndpoint()
                        .findLatestDiscountsByProductIdAndCouponCodes(soapContext,
                                key.getProductId(), new ArrayList<>(key.getCouponCodes()));

                if (CollectionUtils.isEmpty(discounts)) {
                    return new ArrayList<>();
                }
                List<com.active.services.cart.domain.Discount> discount =
                        discounts.stream()
                                .map(disc -> DiscountMapper.MAPPER.toDiscount(disc, context))
                                .collect(Collectors.toList());

                return items.stream().map(item ->
                    CartItemDiscounts.builder().cartItem(item).discounts(discount).build())
                    .collect(Collectors.toList());
            };

            tasks.add(task);
        });

        List<CartItemDiscounts> results = new ArrayList<>();
        CollectionUtils.emptyIfNull(taskRunner.run(tasks).getResults()).forEach(r ->
                results.addAll((List<CartItemDiscounts>) r)
        );
        return results;
    }

    private List<Long> filterQualifiedDiscountIds(List<Long> discountIds) {
        Map<Long, DiscountUsage> discountUsageMap =
                CollectionUtils.emptyIfNull(getDiscountUsage(discountIds)).stream()
                        .collect(Collectors.toMap(DiscountUsage::getDiscountId, Function.identity()));
        return discountIds.stream().filter(id -> discountUsageMap.get(id) != null && (discountUsageMap.get(id).getLimit() == -1 ||
                discountUsageMap.get(id).getUsage() < discountUsageMap.get(id).getLimit())).collect(Collectors.toList());
    }

    private List<DiscountUsage> getDiscountUsage(List<Long> discountIds) {
        GetDiscountUsageReq getDiscountUsageReq = new GetDiscountUsageReq();
        getDiscountUsageReq.setDiscountIds(discountIds);
        GetDiscountUsageRsp rsp = productService.getDiscountUsages(getDiscountUsageReq);
        return rsp.getDiscountUsages();
    }

    public List<DiscountUsage> loadDiscountUsage(List<CartItemDiscounts> cartItemDiscounts){
        List<Long> discountIds = CollectionUtils.emptyIfNull(cartItemDiscounts).stream()
                .map(cid -> cid.getAllDiscountIds())
                .flatMap(Collection::stream).distinct().collect(Collectors.toList());
        return getDiscountUsage(discountIds);
    }

    private Optional<FindLatestDiscountsByProductIdAndCouponCodesKey> cartItemCouponKey(CartQuoteContext context,
                                                                                        CartItem cartItem) {
        Set<String> requestedCodes = new HashSet<>();
        Optional.ofNullable(context.getCartLevelCouponCodes()).ifPresent(requestedCodes::addAll);
        Optional.ofNullable(cartItem.getCouponCodes()).ifPresent(requestedCodes::addAll);

        if (requestedCodes.isEmpty()) {
            return Optional.empty();
        }

        FindLatestDiscountsByProductIdAndCouponCodesKey key = new FindLatestDiscountsByProductIdAndCouponCodesKey();
        key.setCouponCodes(requestedCodes);
        key.setProductId(cartItem.getProductId());

        return Optional.of(key);
    }


    @Data
    class FindLatestDiscountsByProductIdAndCouponCodesKey {

        private Long productId;

        private Set<String> couponCodes;

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public void setCouponCodes(Set<String> couponCodes) {
            this.couponCodes = couponCodes;
        }
    }
}
