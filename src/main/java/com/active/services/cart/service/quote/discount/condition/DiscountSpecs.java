package com.active.services.cart.service.quote.discount.condition;

import com.active.services.cart.application.CartItemSelector;
import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.MembershipMetadata;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.Discount;
import com.active.services.product.discount.multi.MultiDiscountType;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiscountSpecs {
    private final CartItemSelector flattenCartItemSelector;
    private final ProductServiceSoap productRepo;

    public DiscountSpecification membershipDiscount(Long membershipId, String person, Cart cart,
                                                           DateTime evaluateDt, MembershipDiscountsHistory md) {
        return DiscountSequentialSpecs.allOf(
                new NotExpiredSpec(md.getStartDate(), md.getEndDate(), evaluateDt),
                DiscountOrSpec.anyOf(
                        new MembershipInMetadataSpec(membershipId, person, new MembershipMetadata()),
                        new PurchaseRelatedMembershipProductSpec(membershipId, person,
                                cart, flattenCartItemSelector, productRepo))
                );
    }

    public DiscountSpecification couponDiscount(Discount discount, String inputCode, DateTime evaluateDt,
                                                String formXml, Long cartId, Long cartItemId, Integer quantity) {
        return DiscountSequentialSpecs.allOf(
                new CouponCodeSpec(discount.getCouponCode(), inputCode),
                new NotExpiredSpec(discount.getStartDate(), discount.getEndDate(), evaluateDt),
                new XmlRestrictionSpec(discount.getRestrictionsExpression(), formXml),
                new UsageLimitSpec(productRepo, cartId, cartItemId, quantity, discount)
        );
    }

    public DiscountSpecification multiDiscountThreshold(MultiDiscountType type, List<CartItem> items, int threshold) {
        if (type == MultiDiscountType.MULTI_PERSON) {
            return new NumberOfPersonGEThanThresholdSpec(items, threshold);
        } else {
            return new NumberOfProdGEThanThresholdSpec(items, threshold);
        }
    }

    public DiscountSpecification couponDiscount(Discount discount, String couponCode, DateTime evaluateDt, String formXml,
                                                CartQuoteContext context, CartItem cartItem) {
            return DiscountSequentialSpecs.allOf(
                    new CouponCodeSpec(discount.getCouponCode(), couponCode),
                    new NotExpiredSpec(discount.getStartDate(), discount.getEndDate(), evaluateDt),
                    new XmlRestrictionSpec(discount.getRestrictionsExpression(), formXml),
                    new UsageLimitSpec(productRepo, context.getCart().getId(), cartItem.getId(),
                            cartItem.getQuantity(), discount),
                    new UniqueUsedSpec(context.getDiscountModel(cartItem.getProductId()), discount.getId(), context.getUsedCouponDiscountIds())
            );
    }
}
