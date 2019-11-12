package com.active.services.cart.application.impl;

import com.active.services.DiscountModel;
import com.active.services.cart.domain.cart.Cart;
import com.active.services.cart.domain.cart.CartItem;
import com.active.services.cart.domain.discount.CartItemDiscountApplication;
import com.active.services.cart.domain.discount.Discount;
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

/**
 * Coupon discount at order line level is ignored at the point of time, as the original request to have line level
 * coupon was to prevent it to be applied to rest lines in order which can be potentially addressed correctly by
 * enforce discount condition, see https://jirafnd.dev.activenetwork.com/browse/ENDR-20371 for more.
 *
 * Also bypass validations for recurring orders are not implemented for a similar reason.
 */
@Service
@RequiredArgsConstructor
public class CouponDiscountEngine {
    @NonNull private final ProductRepository productRepo;
    @NonNull private final DiscountSpecifications specs;

    public void apply(Cart cart, String coupon) {
        for (CartItem it : cart.getCartItems()) {
            DiscountModel model = productRepo.getProduct(it.getProductId())
                    .map(Product::getDiscountModel)
                    .orElse(DiscountModel.COMBINABLE_FLAT_FIRST);

            List<com.active.services.product.Discount> couponDiscs = productRepo.findDiscountByProductIdAndCode(it.getProductId(), coupon);

            List<Discount> discounts = new ArrayList<>(couponDiscs.size());
            for (com.active.services.product.Discount disc : couponDiscs) {
                Discount discount = new Discount(disc.getName(), disc.getDescription(), disc.getAmount(), disc.getAmountType());
                discount.setCondition(specs.couponDiscount(disc, coupon, new DateTime(LocalDateTime.now()), null, cart.getId(), it.getId(), it.getQuantity()));
                discounts.add(discount);
            }

            new CartItemDiscountApplication(it, discounts, DiscountsAlgorithms.getAlgorithm(model), cart.getCurrency()).apply();
        }
    }
}
