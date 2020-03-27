package com.active.services.cart.service.quote;

import com.active.services.DiscountModel;
import com.active.services.ProductType;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.product.DiscountType;
import com.active.services.product.Product;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Getter
public class CartQuoteContext {
    private static ThreadLocal<CartQuoteContext> threadLocal = new ThreadLocal<>();

    private final Cart cart;

    private final List<CartItem> flattenCartItems;

    private Map<Long, Product> productsMap = new HashMap<>();

    private List<Discount> appliedDiscounts = new ArrayList<>();

    @Setter
    private boolean isAaMember;

    public CartQuoteContext(@NonNull Cart cart) {
        this.cart = cart;
        this.flattenCartItems = emptyIfNull(cart.getFlattenCartItems());
    }

    public List<Long> getProductIds() {
        return flattenCartItems.stream().map(CartItem::getProductId).distinct().collect(Collectors.toList());
    }

    public void setProducts(List<Product> products) {
        productsMap = CollectionUtils.emptyIfNull(products).stream().collect(Collectors.toMap(Product::getId,
                Function.identity()));
    }

    public DiscountModel getDiscountModel(Long productId) {
        return Optional.ofNullable(productsMap.get(productId)).map(Product::getDiscountModel)
                .orElse(DiscountModel.COMBINABLE_FLAT_FIRST);
    }

    public Set<String> getCartLevelCouponCodes() {
        return cart.getCouponCodes();
    }

    /**
     * Add discount to applied list.
     *
     * @param discount
     * @return existing discountId + type discount or else new one
     */
    public Discount addAppliedDiscount(Discount discount) {
        if (discount.getDiscountType() != DiscountType.MULTI) {
            // Multi discount should not check uniqueness, as the discount is from tier.
            Optional<Discount> foundDiscOpt = appliedDiscounts.stream().filter(target ->
                    Objects.equals(target.getDiscountId(), discount.getDiscountId()) &&
                            target.getDiscountType() == discount.getDiscountType()).findAny();

            if (foundDiscOpt.isPresent()) {
                return foundDiscOpt.get();
            }
        }

        // Multi discount and not added discount will be always added to applied list.
        appliedDiscounts.add(discount);

        return discount;
    }

    public boolean hasCartItemWithType(ProductType type) {
        return getProductsMap().values().stream().anyMatch(product -> product.getProductType() == type);
    }

    public Currency getCurrency() {
        return Currency.getInstance(cart.getCurrencyCode());
    }

    /**
     * Sets Context in threadlocal
     *
     * @param context The Context
     */
    public static void set(CartQuoteContext context) {
        threadLocal.set(context);
    }

    public static void destroy() {
        threadLocal.remove();
    }

    public static CartQuoteContext get() {
        return threadLocal.get();
    }
}
