package com.active.services.cart.service.quote.discount.coupon;

import com.active.platform.concurrent.Task;
import com.active.platform.concurrent.TaskRunner;
import com.active.services.Context;
import com.active.services.ContextWrapper;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountConvertor;
import com.active.services.cart.service.quote.discount.DiscountLoader;
import com.active.services.product.Discount;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 *
 * Batch load product service available product/coupon mapping to improve performance.
 *
 */
@Builder
public class CouponDiscountLoader implements DiscountLoader {

    private CartQuoteContext context;
    private SOAPClient soapClient;
    private TaskRunner taskRunner;

    /**
     * Get product service available discounts builder for future filter.
     *
     * @return CartItemDiscounts
     */
    @Override
    public List<CartItemDiscounts> load() {
        // Filter qualified cart items
        List<CartItem> cartItems = context.getCart().getFlattenCartItems().stream()
                .filter(cartItemDisc -> cartItemDisc.getNetPrice().compareTo(BigDecimal.ZERO) >= 0)
                .collect(Collectors.toList());
        // Group cart item by productId + couponCodes.
        Map<FindLatestDiscountsByProductIdAndCouponCodesKey, List<CartItem>> couponTargetsByKey =
                cartItems.stream().filter(cartItem -> cartItemCouponKey(context, cartItem).isPresent())
                        .collect(groupingBy(cartItem -> cartItemCouponKey(context, cartItem).get()));

        if (MapUtils.isEmpty(couponTargetsByKey)) {
            return new ArrayList<>();
        }

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
                List<com.active.services.cart.service.quote.discount.DiscountApplication> discountsWithCondition =
                        discounts.stream().map(disc -> DiscountConvertor.convert(disc, context)).collect(Collectors.toList());

                return items.stream().map(item ->
                    CartItemDiscounts.builder().cartItem(item).discounts(discountsWithCondition).build())
                    .collect(Collectors.toList());
            };

            tasks.add(task);
        });

        List<CartItemDiscounts> results = new ArrayList<>();
        CollectionUtils.emptyIfNull(taskRunner.run(tasks).getResults()).forEach(r ->
            results.addAll((List<CartItemDiscounts>) r)
        );

        // Sort CartItemDiscounts by cartItem net price in reverse order.
        //
        // For example, given the following 3 cart items and percent MOST_EXPENSIVE discount 20% <br>
        // cart item 1 = 100 <br>
        // cart item 2 = 200 <br>
        // cart item 3 = 80 <br>
        // the discount will only apply for cart item 2. cart item discount fee amount = 40.
        return CollectionUtils.emptyIfNull(results).stream()
                .sorted(Comparator.comparing(CartItemDiscounts::getNetPrice).reversed())
                .collect(Collectors.toList());
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
