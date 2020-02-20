package com.active.services.cart.service.quote.discount.coupon;

import com.active.platform.concurrent.Task;
import com.active.platform.concurrent.TaskRunner;
import com.active.services.Context;
import com.active.services.ContextWrapper;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.Discount;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

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
public class CartItemCouponsLoader {
    private CartQuoteContext context;

    private SOAPClient soapClient;

    private TaskRunner taskRunner;

    public CartItemCouponsLoader context(CartQuoteContext context) {
        this.context = context;

        return this;
    }

    public CartItemCouponsLoader soapClient(SOAPClient soapClient) {
        this.soapClient = soapClient;

        return this;
    }

    public CartItemCouponsLoader taskRunner(TaskRunner taskRunner) {
        this.taskRunner = taskRunner;

        return this;
    }
    /**
     * Get product service available discounts builder for future filter.
     *
     * @return
     */
    public List<CartItemCoupons> load() {
        // Filter qualified cart items
        List<CartItem> cartItems = context.getCart().getFlattenCartItems()
                .stream().filter(item -> item.getNetPrice().compareTo(BigDecimal.ZERO) >= 0)
                .sorted(Comparator.comparing(CartItem::getNetPrice).reversed()).collect(Collectors.toList());

        // Group cart item by productId + couponCodes.
        Map<FindLatestDiscountsByProductIdAndCouponCodesKey, List<CartItem>> couponTargetsByKey =
                cartItems.stream().filter(cartItem -> cartItemCouponKey(context, cartItem).isPresent())
                        .collect(groupingBy(cartItem -> cartItemCouponKey(context, cartItem).get()));

        Context soapContext = ContextWrapper.get();
        List<Task<List<CartItemCoupons>>> tasks = new ArrayList<>();
        couponTargetsByKey.forEach((key, items) -> {
            // Build task for each product + couponCode combination.
            Task<List<CartItemCoupons>> task = () -> {
                List<Discount> discounts = soapClient.getProductOMSEndpoint()
                        .findLatestDiscountsByProductIdAndCouponCodes(soapContext,
                                key.getProductId(), new ArrayList<>(key.getCouponCodes()));

                if (CollectionUtils.isEmpty(discounts)) {
                    return new ArrayList<>();
                }

                return items.stream().map(item ->
                    CartItemCoupons.builder().cartItem(item).couponDiscounts(discounts).build())
                    .collect(Collectors.toList());
            };

            tasks.add(task);
        });

        List<CartItemCoupons> results = new ArrayList<>();
        taskRunner.run(tasks).getResults().forEach(r ->
            results.addAll((List<CartItemCoupons>) r)
        );

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
