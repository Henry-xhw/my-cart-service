package com.active.services.cart.service.quote.discount.membership;

import com.active.services.ActiveEntityNotFoundException;
import com.active.services.ContextWrapper;
import com.active.services.ProductType;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.DiscountType;
import com.active.services.product.ProductMembership;
import com.active.services.product.api.omsOnly.types.FindLatestMembershipDiscountsByProductIdsRsp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.active.services.cart.service.quote.discount.DiscountFeeLoader.applyDiscount;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.collections4.ListUtils.union;

@Slf4j
@Component
@RequiredArgsConstructor
public class MembershipDiscountPricer implements CartPricer {

    @NonNull
    private final SOAPClient soapClient;

    private CartQuoteContext context;

    @Override
    public void quote(CartQuoteContext context) {
        if (!isQualifying(context)) {
            LOG.info("Invalid arguments for CartQuoteContext, then return.");
            return;
        }

        this.context = context;

        Set<Long> nonMembershipProductIds = context.getProductsMap().values().stream()
            .filter(p -> p.getProductType() != ProductType.MEMBERSHIP).map(p -> p.getId()).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(nonMembershipProductIds)) {
            return;
        }

        // non-membership's mapping productId and MembershipDiscountsHistory
        Map<Long, List<MembershipDiscountsHistory>> productMembershipDiscountMap =
                getProductMembershipDiscountHistory(nonMembershipProductIds);
        if (MapUtils.isEmpty(productMembershipDiscountMap)) {
            return;
        }

        List<CartItem> cartItems = context.getCart().getFlattenCartItems();
        Map<Long, List<CartItem>> productCartItemMap =  cartItems.stream().collect(groupingBy(CartItem::getProductId));
        List<ProductMembership> productMemberships = getProductMemberships(productCartItemMap.keySet());
        Map<Long, List<CartItem>> membershipIdCartItemMap =
                createMembershipIdCartItemMap(productMemberships, productCartItemMap);

        cartItems.stream().filter(cartItem -> isQualifyProduct(productMembershipDiscountMap, cartItem.getProductId()))
            .forEach(cartItem -> {
                applyMembershipDiscount(productMembershipDiscountMap, membershipIdCartItemMap, cartItem);
            });
    }

    private void applyMembershipDiscount(Map<Long, List<MembershipDiscountsHistory>> productMembershipDiscountMap,
                                         Map<Long, List<CartItem>> membershipIdCartItemMap, CartItem cartItem) {

        MembershipDiscountsHistory membershipDiscount = new MembershipDiscountProcessor(productMembershipDiscountMap,
                membershipIdCartItemMap, cartItem, context.getCurrency()).apply();
        if (membershipDiscount == null) {
            return;
        }

        Discount discount = Discount.builder().cartId(context.getCart().getId()).name(membershipDiscount.getName())
                .description(membershipDiscount.getDescription()).discountId(membershipDiscount.getId())
                .discountType(DiscountType.MEMBERSHIP).amount(membershipDiscount.getAmount())
                .amountType(membershipDiscount.getAmountType()).build();
        discount.setIdentifier(UUID.randomUUID());

        // create CartItemFee(Discount)
        applyDiscount(context, cartItem.getPriceCartItemFee().get(), discount, discount.getAmount(), 1);
    }

    private boolean isQualifyProduct(Map<Long, List<MembershipDiscountsHistory>> productDiscountMap, Long productId) {
        return CollectionUtils.isNotEmpty(productDiscountMap.get(productId))
                && context.getProductsMap().get(productId).getProductType() != ProductType.MEMBERSHIP;
    }

    private Map<Long, List<CartItem>> createMembershipIdCartItemMap(final List<ProductMembership> productMemberships,
            final Map<Long, List<CartItem>> productCartItemMap) {

        return CollectionUtils.emptyIfNull(productMemberships).stream().collect(HashMap::new, (m, v) -> {
            m.put(v.getMembershipId(), union(m.get(v.getMembershipId()), productCartItemMap.get(v.getProductId())));
        }, HashMap::putAll);
    }

    private List<ProductMembership> getProductMemberships(Set<Long> productIds) {
        try {
            return soapClient.getProductOMSEndpoint().findProductMembershipsForProductIds(ContextWrapper.get(),
                    new ArrayList<>(productIds));
        } catch (ActiveEntityNotFoundException e) {
            LOG.info("Unable to find ProductMemberships.", e);
        }

        return new ArrayList<>();
    }

    private Map<Long, List<MembershipDiscountsHistory>> getProductMembershipDiscountHistory(Set<Long> productIds) {

        List<FindLatestMembershipDiscountsByProductIdsRsp> membershipDiscounts =
                soapClient.getProductOMSEndpoint().findLatestMembershipDiscountsByProductIds(ContextWrapper.get(),
                new ArrayList<>(productIds));

        if (CollectionUtils.isEmpty(membershipDiscounts)) {
            return null;
        }

        return membershipDiscounts.stream().collect(toMap(FindLatestMembershipDiscountsByProductIdsRsp::getProductId,
                FindLatestMembershipDiscountsByProductIdsRsp::getHistories));
    }

    private boolean isQualifying(CartQuoteContext context) {
        return context != null && context.getCart() != null && isNotEmpty(context.getCart().getItems());
    }

}