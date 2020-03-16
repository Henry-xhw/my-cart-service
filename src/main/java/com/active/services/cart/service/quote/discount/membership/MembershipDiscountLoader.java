package com.active.services.cart.service.quote.discount.membership;

import com.active.services.ActiveEntityNotFoundException;
import com.active.services.ContextWrapper;
import com.active.services.ProductType;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountLoader;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.DiscountType;
import com.active.services.product.Product;
import com.active.services.product.ProductMembership;
import com.active.services.product.api.omsOnly.types.FindLatestMembershipDiscountsByProductIdsRsp;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Builder
public class MembershipDiscountLoader implements DiscountLoader {
    private CartQuoteContext context;
    private SOAPClient soapClient;

    @Override
    public List<CartItemDiscounts> load() {

        Set<Long> nonMembershipProductIds = context.getProductsMap().values().stream()
                .filter(p -> p.getProductType() != ProductType.MEMBERSHIP).map(Product::getId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(nonMembershipProductIds)) {
            return new ArrayList<>();
        }

        // non-membership's mapping productId and MembershipDiscountsHistory
        Map<Long, List<MembershipDiscountsHistory>> productMembershipDiscountMap =
                loadProductMembershipDiscountHistory(nonMembershipProductIds);
        if (MapUtils.isEmpty(productMembershipDiscountMap)) {
            return new ArrayList<>();
        }

        return context.getCart().getFlattenCartItems().stream()
                .filter(cartItem -> isQualifyProduct(context, cartItem.getProductId(),
                        productMembershipDiscountMap.get(cartItem.getProductId())))
                .map(cartItem -> CartItemDiscounts.builder().cartItem(cartItem)
                        .discounts(getDiscounts(productMembershipDiscountMap.get(cartItem.getProductId()))).build())
                .collect(Collectors.toList());
    }

    public List<Long> loadNewItemMembershipIds() {
        Map<Long, List<CartItem>> productCartItemMap =  context.getCart().getFlattenCartItems().stream().collect(groupingBy(CartItem::getProductId));
        List<ProductMembership> productMemberships = getProductMemberships(productCartItemMap.keySet());
        return productMemberships.stream().map(ProductMembership::getMembershipId).collect(Collectors.toList());
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


    private List<Discount> getDiscounts(List<MembershipDiscountsHistory> membershipDiscountsHistories) {
        return membershipDiscountsHistories.stream().map(mdh -> buildDiscount(context, mdh)).collect(Collectors.toList());
    }

    private Map<Long, List<MembershipDiscountsHistory>> loadProductMembershipDiscountHistory(Set<Long> productIds) {

        List<FindLatestMembershipDiscountsByProductIdsRsp> membershipDiscounts =
                soapClient.getProductOMSEndpoint().findLatestMembershipDiscountsByProductIds(ContextWrapper.get(),
                        new ArrayList<>(productIds));

        if (CollectionUtils.isEmpty(membershipDiscounts)) {
            return null;
        }

        return membershipDiscounts.stream().collect(toMap(FindLatestMembershipDiscountsByProductIdsRsp::getProductId,
                FindLatestMembershipDiscountsByProductIdsRsp::getHistories));
    }

    private boolean isQualifyProduct(CartQuoteContext context, Long productId,
                                     List<MembershipDiscountsHistory> productDiscounts) {
        return CollectionUtils.isNotEmpty(productDiscounts) &&
                context.getProductsMap().get(productId).getProductType() != ProductType.MEMBERSHIP;
    }


    private Discount buildDiscount(CartQuoteContext context, MembershipDiscountsHistory membershipDiscount) {
        Discount discount = new Discount();
        discount.setCartId(context.getCart().getId());
        discount.setName(membershipDiscount.getName());
        discount.setDescription(membershipDiscount.getDescription());
        discount.setDiscountId(membershipDiscount.getId());
        discount.setDiscountType(DiscountType.MEMBERSHIP);
        discount.setAmount(membershipDiscount.getAmount());
        discount.setAmountType(membershipDiscount.getAmountType());
        discount.setIdentifier(UUID.randomUUID());
        discount.setMembershipId(membershipDiscount.getMembershipId());
        return discount;
    }
}
