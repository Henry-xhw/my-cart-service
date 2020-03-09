package com.active.services.cart.service.quote.discount.membership;

import com.active.services.cart.domain.CartItem;
import com.active.services.domain.DateTime;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.AmountType;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.list.SetUniqueList;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.active.services.cart.service.quote.discount.DiscountAmountCalcUtil.calcFlatAmount;
import static com.active.services.oms.OmsUtil.isOnOrBeforeBizDate;

public class MembershipDiscountProcessor {

    private CartItem cartItem;

    private Currency currency;

    private Map<Long, List<CartItem>> membershipIdCartItemMap;

    private Map<Long, List<MembershipDiscountsHistory>> productMembershipDiscountMap;

    public MembershipDiscountProcessor(Map<Long, List<MembershipDiscountsHistory>> productMembershipDiscountMap,
                                       Map<Long, List<CartItem>> membershipIdCartItemMap,
                                       CartItem cartItem, Currency currency) {
        this.productMembershipDiscountMap = productMembershipDiscountMap;
        this.membershipIdCartItemMap = membershipIdCartItemMap;
        this.cartItem = cartItem;
        this.currency = currency;
    }

    public MembershipDiscountsHistory apply() {
        List<MembershipDiscountsHistory> membershipDiscountsHistories = new ArrayList<>();
        membershipDiscountsHistories.addAll(filterMembershipDiscountByMetaData());
        membershipDiscountsHistories.addAll(filterMembershipDiscountByCartItem());
        membershipDiscountsHistories = SetUniqueList.setUniqueList(membershipDiscountsHistories);
        if (CollectionUtils.isEmpty(membershipDiscountsHistories)) {
            return null;
        }

       return determineWhichDiscount(membershipDiscountsHistories);
    }

    private List<MembershipDiscountsHistory> filterMembershipDiscountByMetaData() {
        if (cartItem.getMembershipId() == null || !productMembershipDiscountMap.containsKey(cartItem.getProductId())) {
            return Collections.emptyList();
        }

        return productMembershipDiscountMap.get(cartItem.getProductId()).stream()
                .filter(md -> md.getMembershipId().equals(cartItem.getMembershipId())).collect(Collectors.toList());
    }

    private List<MembershipDiscountsHistory> filterMembershipDiscountByCartItem() {
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

    private MembershipDiscountsHistory determineWhichDiscount(List<MembershipDiscountsHistory> membershipDiscounts) {
        BigDecimal discountAmt = BigDecimal.ZERO;
        MembershipDiscountsHistory discountToUse = null;
        DateTime currentDateTime = new DateTime(new Date());
        for (final MembershipDiscountsHistory discount : membershipDiscounts) {
            if (!isValidMembershipDiscount(discount, currentDateTime)) {
                continue;
            }

            BigDecimal calculatedAmount = calculateDiscountAmount(cartItem, discount);
            if (calculatedAmount.compareTo(discountAmt) < 0 || calculatedAmount.compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            discountAmt = calculatedAmount;

            MembershipDiscountsHistory highestDiscountGiven = selectHighestDiscount(discount, discountToUse);
            if (discountToUse != null && !Objects.equals(discountToUse.getId(), highestDiscountGiven.getId())) {
                // Add active but lower-amount-given discounts to the non-applied set in results
            }

            discountToUse = highestDiscountGiven;
            discountToUse.setAmount(calculatedAmount);
        }

        return discountToUse;
    }

    private MembershipDiscountsHistory selectHighestDiscount(MembershipDiscountsHistory proposedNewDisc,
                                                             MembershipDiscountsHistory existingDisc) {
        MembershipDiscountsHistory discountToReturn = existingDisc;
        if (existingDisc == null) {
            discountToReturn = clone(proposedNewDisc);
            return discountToReturn;
        }

        BigDecimal proposedNewDiscountAmount = calculateDiscountAmount(cartItem, proposedNewDisc);
        BigDecimal existingDiscountAmount = calculateDiscountAmount(cartItem, existingDisc);

        if (existingDiscountAmount.compareTo(proposedNewDiscountAmount) < 0) {
            discountToReturn = clone(proposedNewDisc);
            return discountToReturn;
        }

        return discountToReturn;
    }

    private BigDecimal calculateDiscountAmount(final CartItem cartItem, MembershipDiscountsHistory discount) {
        BigDecimal discountAmount = calcFlatAmount(cartItem.getNetPrice(), discount.getAmount(),
                discount.getAmountType(), currency);
        return discountAmount.compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO : discountAmount;
    }

    private MembershipDiscountsHistory clone(MembershipDiscountsHistory from) {
        return (MembershipDiscountsHistory) from.deepCopy();
    }

    private boolean isValidMembershipDiscount(MembershipDiscountsHistory discount, DateTime businessDate) {
        Instant startDate = defaultInstant(discount.getStartDate(), Instant.MIN);
        Instant endDate = defaultInstant(discount.getEndDate(), Instant.MAX);
        Instant businessInstant = businessDate.toDate().toInstant();
        return !startDate.isAfter(businessInstant) && !businessInstant.isAfter(endDate);
    }

    private Instant defaultInstant(DateTime dateTime, Instant defaultValue) {
        return dateTime == null ? defaultValue : dateTime.toDate().toInstant();
    }

}


