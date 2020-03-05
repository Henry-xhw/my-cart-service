package com.active.services.cart.service.quote;

import com.active.services.DiscountModel;
import com.active.services.ProductType;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.product.DiscountAlgorithm;
import com.active.services.product.DiscountType;
import com.active.services.product.Product;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
public class CartQuoteContext {
    private final Cart cart;

    private Map<Long, Product> productsMap = new HashMap<>();

    private List<Discount> appliedDiscounts = new ArrayList<>();

    @Setter
    private boolean isAaMember;

    public List<Long> getProductIds() {
        return cart.getFlattenCartItems().stream().map(CartItem::getProductId).distinct().collect(Collectors.toList());
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

    public CartQuoteContext addAppliedDiscount(Discount discount) {
        if (appliedDiscounts == null) {
            appliedDiscounts = new ArrayList<>();
        }
        appliedDiscounts.add(discount);
        return this;
    }

    public List<Long> getUsedUniqueCouponDiscountsIds() {
        return CollectionUtils.emptyIfNull(appliedDiscounts).stream()
                .filter(discount -> discount.getDiscountType() ==
                        DiscountType.COUPON && discount.getAlgorithm() == DiscountAlgorithm.MOST_EXPENSIVE)
                .map(Discount::getDiscountId).collect(Collectors.toList());
    }

    public Currency getCurrency() {
        return Currency.getInstance(cart.getCurrencyCode());
    }

    public boolean hasCartItemWithType(ProductType type) {
        return getProductsMap().values().stream().anyMatch(product -> product.getProductType() == type);
    }

}
