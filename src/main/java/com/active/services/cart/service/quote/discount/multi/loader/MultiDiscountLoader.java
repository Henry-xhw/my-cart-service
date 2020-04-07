package com.active.services.cart.service.quote.discount.multi.loader;

import com.active.platform.concurrent.Task;
import com.active.platform.concurrent.TaskRunner;
import com.active.services.ContextWrapper;
import com.active.services.cart.domain.CartItem;
import com.active.services.domain.DateTime;
import com.active.services.product.api.omsOnly.soap.ProductOMSEndpoint;
import com.active.services.product.discount.multi.MultiDiscount;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

/**
 *
 * Loader to load product service effective multi discount by product id + business date.
 *
 * Return one MultiDiscountCartItem for each multi discount.
 *
 */
@Component
@Slf4j
public class MultiDiscountLoader {

    @Autowired
    private TaskRunner taskRunner;

    @Autowired
    private ProductOMSEndpoint productOMSEndpoint;

    /**
     * One MultiDiscountCartItem for each multi discount.
     *
     */
    public List<MultiDiscountCartItem> load(List<CartItem> flattenCartItems) {
        // Group effective cart items by product id
        Map<Long, List<CartItem>> itemsMap = flattenCartItems.stream().filter(item ->
                !item.isIgnoreMultiDiscounts() && item.hasPersonIdentifier())
                .collect(Collectors.groupingBy(CartItem::getProductId));

        if (itemsMap.isEmpty()) {
            return new ArrayList<>();
        }

        Instant now = Instant.now();

        // Build tasks
        List<Task<List<MultiDiscountCartItem>>> tasks = new ArrayList<>();
        itemsMap.forEach((key, items) -> {
            Task<List<MultiDiscountCartItem>> task = () -> {
                Set<MultiDiscount> mds = new HashSet<>(emptyIfNull(
                    productOMSEndpoint.findEffectiveMultiDiscountsWithDate(ContextWrapper.get(), key,
                    new DateTime(Date.from(now)))));
                return mds.stream().map(md -> {
                    MultiDiscountCartItem multiDiscountCartItem = new MultiDiscountCartItem(md);
                    multiDiscountCartItem.addCartItems(items);

                    return multiDiscountCartItem;
                }).collect(Collectors.toList());
            };

            tasks.add(task);
        });

        List<MultiDiscountCartItem> results = new ArrayList<>();
        CollectionUtils.emptyIfNull(taskRunner.run(tasks).getResults()).forEach(r ->
            results.addAll((List<MultiDiscountCartItem>) r)
        );

        // Need merge the results.
        // The concurrent load results may be like:
        // md1: item1, item2
        // md1: item1, item3
        // We should return it as:
        // md1: item1, item2, item3.
        return mergeResults(results);
    }

    /**
     * Merge by same multi discount id.
     *
     * @param results
     * @return
     */
    private List<MultiDiscountCartItem> mergeResults(List<MultiDiscountCartItem> results) {
        Map<MultiDiscount, MultiDiscountCartItem> itemByMultiDiscountId = new HashMap<>();

        results.forEach(r -> {
            MultiDiscount md = r.getMultiDiscount();

            if (CollectionUtils.isEmpty(md.getThresholdSettings()) && CollectionUtils.isEmpty(md.getTiers())) {
                LOG.info("MultiDiscount (with single threshold) has no tiers: {}", md);
            } else {
                MultiDiscountCartItem foundItem = itemByMultiDiscountId.get(md);
                if (foundItem == null) {
                    foundItem = new MultiDiscountCartItem(md);
                    itemByMultiDiscountId.put(md, foundItem);
                }
                foundItem.addCartItems(r.getCartItems());
            }
        });

        return new ArrayList<>(itemByMultiDiscountId.values());
    }
}
