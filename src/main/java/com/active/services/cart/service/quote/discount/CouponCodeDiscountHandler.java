package com.active.services.cart.service.quote.discount;

import com.active.services.DiscountModel;
import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.CouponMode;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.algorithm.BestDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.DiscountAlgorithm;
import com.active.services.cart.service.quote.discount.algorithm.StackableFlatFirstDiscountAlgorithm;
import com.active.services.cart.service.quote.discount.condition.DiscountSequentialSpecs;
import com.active.services.cart.service.quote.discount.condition.DiscountSpecification;
import com.active.services.cart.service.quote.discount.condition.NotExpiredSpec;
import com.active.services.cart.service.quote.discount.condition.UniqueUsedSpec;
import com.active.services.cart.service.quote.discount.condition.UsageLimitSpec;
import com.active.services.domain.DateTime;
import com.active.services.product.DiscountType;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CouponCodeDiscountHandler implements DiscountHandler {

    @NonNull private final ProductServiceSoap productRepo;
    @NonNull private final CartQuoteContext context;
    @NonNull private final CartItem item;

    @Override
    public List<Discount> loadDiscounts() {
        List<com.active.services.product.Discount> couponDiscs = getCouponCodeDiscounts();
        if (CollectionUtils.isEmpty(couponDiscs)) {
            return new ArrayList<>();
        }
        List<Discount> discounts = couponDiscs.stream().map(disc -> convert(disc))
                .filter(Discount::satisfy).collect(Collectors.toList());
        return getHighPriorityDiscounts(discounts);
    }

    @Override
    public DiscountAlgorithm getDiscountAlgorithm() {
        return context.getDiscountModel(item.getProductId()) == DiscountModel.COMBINABLE_FLAT_FIRST ?
                new StackableFlatFirstDiscountAlgorithm() :
                new BestDiscountAlgorithm(item, context.getCurrency());
    }

    private List<Discount> getHighPriorityDiscounts(List<Discount> discounts) {
        List<Discount> cartItemLevelDiscount = new ArrayList<>();
        if (item.getCouponMode() == CouponMode.HIGH_PRIORITY) {
            cartItemLevelDiscount =
                    CollectionUtils.emptyIfNull(discounts).stream().filter(discount ->
                            item.getCouponCodes().contains(discount.getCouponCode())).collect(Collectors.toList());
        }
        return CollectionUtils.isNotEmpty(cartItemLevelDiscount) ?  cartItemLevelDiscount : discounts;
    }


    private Discount convert(com.active.services.product.Discount disc) {
        Discount discount = new Discount(disc.getName(), disc.getDescription(), disc.getAmount(),
                disc.getAmountType(), disc.getId(), DiscountType.COUPON, disc.getCouponCode(),
                disc.getDiscountAlgorithm());
        discount.setCondition(buildDiscountSpecification(context, item, disc));
        return discount;
    }

    private DiscountSpecification buildDiscountSpecification(CartQuoteContext context, CartItem cartItem,
                                                            com.active.services.product.Discount discount) {
        return DiscountSequentialSpecs.allOf(
                new NotExpiredSpec(discount.getStartDate(), discount.getEndDate(), new DateTime(LocalDateTime.now())),
                new UsageLimitSpec(productRepo, context.getCart().getId(), cartItem.getId(), cartItem.getQuantity(),
                        discount),
                new UniqueUsedSpec(discount.getId(), context.getUsedUniqueCouponDiscountsIds())
        );
    }

    private List<com.active.services.product.Discount> getCouponCodeDiscounts() {
        Set<String> couponCodes = getCouponCodes();
        if (CollectionUtils.isEmpty(couponCodes)) {
            return new ArrayList<>();
        }
        return productRepo.findDiscountsByProductIdAndCode(item.getProductId(), new ArrayList<>(couponCodes));
    }

    private Set<String> getCouponCodes() {

        Set<String> requestedCodes = new HashSet<>();
        Optional.ofNullable(context.getCartLevelCouponCodes()).ifPresent(requestedCodes::addAll);
        Optional.ofNullable(item.getCouponCodes()).ifPresent(requestedCodes::addAll);

        return requestedCodes;
    }
}

