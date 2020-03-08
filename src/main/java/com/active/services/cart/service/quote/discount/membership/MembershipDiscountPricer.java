package com.active.services.cart.service.quote.discount.membership;

import com.active.services.ActiveEntityNotFoundException;
import com.active.services.ContextWrapper;
import com.active.services.ProductType;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.DiscountType;
import com.active.services.product.ProductMembership;
import com.active.services.product.api.omsOnly.types.FindLatestMembershipDiscountsByProductIdsRsp;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.list.SetUniqueList;
import org.springframework.stereotype.Component;

import static com.active.services.cart.service.quote.discount.DiscountAmountCalcUtil.calcFlatAmount;
import static com.active.services.cart.service.quote.discount.DiscountFeeLoader.applyDiscount;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.active.services.oms.OmsUtil.isOnOrBeforeBizDate;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.ListUtils.union;

@Slf4j
@Component
@RequiredArgsConstructor
public class MembershipDiscountPricer implements CartPricer {

    @NonNull
    private final SOAPClient soapClient;

    private CartQuoteContext context;

    private Map<Long, List<CartItem>> membershipIdCartItemMap;

    /** non-membership's mapping productId and MembershipDiscountsHistory **/
    private Map<Long, List<MembershipDiscountsHistory>> productMembershipDiscountMap;

    @Override
    public void quote(CartQuoteContext context) {
        this.context = context;

        Set<Long> nonMembershipProductIds = context.getProductsMap().values().stream()
            .filter(p -> p.getProductType() != ProductType.MEMBERSHIP).map(p -> p.getId()).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(nonMembershipProductIds)) {
            return;
        }

        // non-membership's mapping productId and MembershipDiscountsHistory
        productMembershipDiscountMap = getProductMembershipDiscountHistory(nonMembershipProductIds);
        if (MapUtils.isEmpty(productMembershipDiscountMap)) {
            return;
        }

        List<CartItem> cartItems = context.getCart().getFlattenCartItems();
        Map<Long, List<CartItem>> productCartItemMap =  cartItems.stream().collect(groupingBy(CartItem::getProductId));
        List<ProductMembership> productMemberships = getProductMemberships(productCartItemMap.keySet());
        membershipIdCartItemMap = createMembershipIdCartItemMap(productMemberships, productCartItemMap);

        cartItems.stream().filter(cartItem -> isQualifyCartItem(cartItem)).forEach(cartItem -> {
            applyMembershipDiscount(cartItem);
        });
    }

    private void applyMembershipDiscount(CartItem cartItem) {
        List<MembershipDiscountsHistory> membershipDiscountsHistories = new ArrayList<>();
        membershipDiscountsHistories.addAll(processMembershipDiscountHistoryByMetaData(cartItem));
        membershipDiscountsHistories.addAll(processMembershipDiscountHistoryByCartItem(cartItem));
        membershipDiscountsHistories = SetUniqueList.setUniqueList(membershipDiscountsHistories);
        if (CollectionUtils.isEmpty(membershipDiscountsHistories)) {
            return;
        }

        MembershipDiscountsHistory membershipDiscount = determineWhichDiscount(cartItem, membershipDiscountsHistories);
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

    private List<MembershipDiscountsHistory> processMembershipDiscountHistoryByCartItem(CartItem cartItem) {
        List<MembershipDiscountsHistory> discountsHistories = productMembershipDiscountMap.get(cartItem.getProductId());
        if (CollectionUtils.isEmpty(discountsHistories)) {
            return Collections.emptyList();
        }

        return discountsHistories.stream().filter(md -> membershipIdCartItemMap.containsKey(md.getMembershipId()))
                .filter(md -> filterMembershipDiscountByCartItem(cartItem.getPersonIdentifier(), md))
                .collect(Collectors.toList());
    }

    private boolean filterMembershipDiscountByCartItem(String personKey, MembershipDiscountsHistory md) {
        List<CartItem> cartItems = membershipIdCartItemMap.get(md.getMembershipId());
        if (CollectionUtils.isEmpty(cartItems)) {
            return false;
        }

        return cartItems.stream().anyMatch(cartItem -> Objects.equals(cartItem.getPersonIdentifier(), personKey));
    }

    private List<MembershipDiscountsHistory> processMembershipDiscountHistoryByMetaData(CartItem cartItem) {
        if (cartItem.getMembershipId() == null
                || !productMembershipDiscountMap.containsKey(cartItem.getProductId())) {
            return Collections.emptyList();
        }

        return productMembershipDiscountMap.get(cartItem.getProductId()).stream()
                .filter(md -> md.getMembershipId().equals(cartItem.getMembershipId())).collect(Collectors.toList());
    }

    private boolean isQualifyCartItem(CartItem cartItem) {
        return CollectionUtils.isNotEmpty(productMembershipDiscountMap.get(cartItem.getProductId()))
                && context.getProductsMap().get(cartItem.getProductId()).getProductType() != ProductType.MEMBERSHIP;
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

    private MembershipDiscountsHistory determineWhichDiscount(final CartItem cartItem,
                                                              final List<MembershipDiscountsHistory> discountList) {

        BigDecimal discountAmount = BigDecimal.ZERO;
        MembershipDiscountsHistory discountToUse = null;
        DateTime currentDateTime = new DateTime(new Date());
        for (final MembershipDiscountsHistory discount : discountList) {
            // check to ensure the discount has not expired, do not use expired discounts
            if (!isQuanlifyMembershipDiscount(discount, currentDateTime)) {
                // Add non-active discounts to the non-applied set in results
                continue;
            }

            // Calculate how much of a discount amount a given discount COULD give on an cartItem
            BigDecimal calculatedDiscountAmount = calculateDiscountAmount(cartItem, discount);

            if (calculatedDiscountAmount.compareTo(discountAmount) < 0
                    || calculatedDiscountAmount.compareTo(BigDecimal.ZERO) <= 0) {
                // Add active but lower-amount-given discounts to the non-applied set in results
                continue;
            }

            // if the flat amount discount is greater than any previous calculated discounts(and greater than 0),
            // use it instead of others

            discountAmount = calculatedDiscountAmount;

            MembershipDiscountsHistory highestDiscountGiven = selectHighestDiscount(cartItem, discount, discountToUse);

            if (discountToUse != null && !discountToUse.getId().equals(highestDiscountGiven.getId())) {
                // Add active but lower-amount-given discounts to the non-applied set in results
            }

            discountToUse = highestDiscountGiven;

            discountToUse.setAmount(calculatedDiscountAmount);
        }

        return discountToUse;
    }

    private boolean isQuanlifyMembershipDiscount(MembershipDiscountsHistory discount, DateTime businessDate) {
        // if discount has no start and end date defined, it is active (i.e. perpetual)
        if (discount.getStartDate() == null && discount.getEndDate() == null) {
            return true;
        }

        DateTime startDate = discount.getStartDate() == null ? new DateTime(Date.from(Instant.MIN)) :
                discount.getStartDate();

        DateTime endDate = discount.getEndDate() == null ? new DateTime(Date.from(Instant.MAX)) :
                discount.getEndDate();

        return isOnOrBeforeBizDate(businessDate, startDate) && isOnOrBeforeBizDate(endDate, businessDate);
    }

    private BigDecimal calculateDiscountAmount(final CartItem cartItem, MembershipDiscountsHistory discount) {
        BigDecimal discountAmount = calcFlatAmount(cartItem.getNetPrice(), discount.getAmount(),
                discount.getAmountType(), context.getCurrency());
        return discountAmount.compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO : discountAmount;
    }

    private MembershipDiscountsHistory selectHighestDiscount(CartItem cartItem,
                                                             MembershipDiscountsHistory proposedNewDisc,
                                                             MembershipDiscountsHistory existingDisc) {

        MembershipDiscountsHistory discountToReturn = existingDisc;
        if (existingDisc == null) {
            discountToReturn = cloneMembershipDiscount(proposedNewDisc);
            return discountToReturn;
        }

        BigDecimal proposedNewDiscountAmount = calculateDiscountAmount(cartItem, proposedNewDisc);
        BigDecimal existingDiscountAmount = calculateDiscountAmount(cartItem, existingDisc);

        // if the total calculated amount discount is the same as any existing (previously) calculated ones, check which discount
        // would have used the highest amount and return that one instead
        if (existingDiscountAmount.compareTo(proposedNewDiscountAmount) < 0) {
            discountToReturn = cloneMembershipDiscount(proposedNewDisc);
            return discountToReturn;
        }

        return discountToReturn;
    }

    private MembershipDiscountsHistory cloneMembershipDiscount(MembershipDiscountsHistory from) {
        MembershipDiscountsHistory membershipDiscountsHistory = new MembershipDiscountsHistory();
        membershipDiscountsHistory.setId(from.getId());
        membershipDiscountsHistory.setAmount(from.getAmount());
        membershipDiscountsHistory.setStartDate(from.getStartDate());
        membershipDiscountsHistory.setEndDate(from.getEndDate());
        membershipDiscountsHistory.setAmountType(from.getAmountType());
        membershipDiscountsHistory.setMembershipId(from.getMembershipId());
        membershipDiscountsHistory.setVersioningGroupId(from.getVersioningGroupId());
        membershipDiscountsHistory.setLatestInVersioningGroup(from.isLatestInVersioningGroup());
        membershipDiscountsHistory.setVersion(from.getVersion());
        membershipDiscountsHistory.setName(from.getName());
        membershipDiscountsHistory.setDescription(from.getDescription());
        membershipDiscountsHistory.setCreatedBy(from.getCreatedBy());
        membershipDiscountsHistory.setCreatedDate(from.getCreatedDate());
        membershipDiscountsHistory.setModifiedDate(from.getModifiedDate());
        membershipDiscountsHistory.setModifiedBy(from.getModifiedBy());
        return membershipDiscountsHistory;
    }

}