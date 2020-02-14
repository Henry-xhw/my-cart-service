package com.active.services.cart.service.quote;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.Cart;
import com.active.services.product.Product;

import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Data
public class CartQuoteContext {
    private Cart cart;
    private List<Product> products;

    private Map<Long, Product> productsMap;

    public DiscountModel getDiscountModel(Long discountId) {
        return Optional.ofNullable(productsMap.get(discountId))
                .map(Product::getDiscountModel)
                .orElse(DiscountModel.COMBINABLE_FLAT_FIRST);
    }

    public CartQuoteContext(Cart cart) {
        this.cart = cart;
    }
}
