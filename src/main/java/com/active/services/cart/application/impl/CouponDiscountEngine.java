package com.active.services.cart.application.impl;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.discount.Discount;
import com.active.services.cart.domain.discount.DiscountApplication;
import com.active.services.cart.domain.discount.algorithm.DiscountsAlgorithms;
import com.active.services.cart.domain.discount.condition.DiscountSpecifications;
import com.active.services.cart.infrastructure.repository.ProductRepository;
import com.active.services.domain.DateTime;
import com.active.services.product.Product;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponDiscountEngine {
    @NonNull private final ProductRepository productRepo;
    @NonNull private final DiscountSpecifications specs;

    public void apply(Cart cart, String coupon) {
        for (CartItem it : cart.getCartItems()) {
            Optional<Product> product = productRepo.getProduct(it.getProductId());
            List<com.active.services.product.Discount> couponDiscs = productRepo.findDiscountByProductIdAndCode(it.getProductId(), coupon);

            DiscountModel model = product.map(Product::getDiscountModel).orElse(DiscountModel.COMBINABLE_FLAT_FIRST);
            List<Discount> discounts = new ArrayList<>(couponDiscs.size());
            for (com.active.services.product.Discount disc : couponDiscs) {
                Discount discount = new Discount(disc.getName(), disc.getDescription(), disc.getAmount(), disc.getAmountType());
                discount.setCondition(specs.couponDiscount(disc, coupon, new DateTime(LocalDateTime.now()), null, cart.getId(), it.getId(), it.getQuantity()));
                discounts.add(discount);
            }

            new DiscountApplication(discounts, DiscountsAlgorithms.getAlgorithm(model)).apply(it, cart.getCurrency());
        }
    }
}
