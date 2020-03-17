package com.active.services.cart.service.quote.discount.coupon;

import com.active.platform.concurrent.Task;
import com.active.platform.concurrent.TaskRunner;
import com.active.services.Context;
import com.active.services.ContextWrapper;
import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.Discount;

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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

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
    public Map<CartItem, List<Discount>> loadDiscounts() {
        // Filter qualified cart items
        // Group cart item by productId + couponCodes.
        Map<FindLatestDiscountsByProductIdAndCouponCodesKey, List<CartItem>> couponTargetsByKey =
                cartItems.stream().filter(cartItem -> cartItemCouponKey(context, cartItem).isPresent())
                        .collect(groupingBy(cartItem -> cartItemCouponKey(context, cartItem).get()));

        if (MapUtils.isEmpty(couponTargetsByKey)) {
            return new HashMap<>();
        }

        return loadCartItemDiscounts(couponTargetsByKey);
    }


    @NotNull
    private Map<CartItem, List<Discount>> loadCartItemDiscounts(
            Map<FindLatestDiscountsByProductIdAndCouponCodesKey, List<CartItem>> couponTargetsByKey) {
        Context soapContext = ContextWrapper.get();
        Map<CartItem, List<Discount>> results = new ConcurrentHashMap<>();

        List<Task<Void>> tasks = new ArrayList<>();
        couponTargetsByKey.forEach((key, items) -> {
            // Build task for each product + couponCode combination.
            Task<Void> task = () -> {
                List<Discount> discounts = soapClient.getProductOMSEndpoint()
                        .findLatestDiscountsByProductIdAndCouponCodes(soapContext,
                                key.getProductId(), new ArrayList<>(key.getCouponCodes()));
                if (CollectionUtils.isNotEmpty(discounts)) {
                    items.stream().forEach(cartItem -> results.put(cartItem, discounts));
                }

                return null;
            };

            tasks.add(task);
        });
        taskRunner.run(tasks).getResults();

        return results;
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
