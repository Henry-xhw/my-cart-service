package com.active.services.cart.service.quote.discount.membership;

import com.active.services.ActiveEntityNotFoundException;
import com.active.services.ContextWrapper;
import com.active.services.ProductType;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartDiscountBasePricer;
import com.active.services.order.discount.membership.MembershipDiscountsHistory;
import com.active.services.product.ProductMembership;
import com.active.services.product.api.omsOnly.types.FindLatestMembershipDiscountsByProductIdsRsp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;

@Component
@Slf4j
public class CartMembershipPricer extends CartDiscountBasePricer {

    @Autowired
    private SOAPClient soapClient;

    @Override
    protected void doQuote(CartQuoteContext context, List<CartItem> noneZeroItems) {

        Set<Long> nonMembershipProductIds =
                noneZeroItems.stream().filter(cartItem ->
                        context.getProductsMap().get(cartItem.getProductId()).getProductType() != ProductType.MEMBERSHIP)
                        .map(CartItem::getProductId).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(nonMembershipProductIds)) {
            return;
        }

        Map<Long, List<MembershipDiscountsHistory>> productDiscounts = loadProductMembershipDiscounts(nonMembershipProductIds);
        if (MapUtils.isEmpty(productDiscounts)) {
            return;
        }

        MembershipDiscountContext membershipDiscountContext = new MembershipDiscountContext();
        membershipDiscountContext.setProductMembershipDiscounts(productDiscounts);
        try {
            List<ProductMembership> productMemberships = soapClient.getProductOMSEndpoint()
                    .findProductMembershipsForProductIds(ContextWrapper.get(), new ArrayList<>(context.getProductIds()));
            membershipDiscountContext.setProductMemberships(productMemberships);
        } catch (ActiveEntityNotFoundException e) {
            LOG.info("Unable to find ProductMemberships.", e);
        }

        noneZeroItems.forEach(noneZeroItem -> getCartItemMembershipPricer(membershipDiscountContext).quote(context,
                noneZeroItem));
    }

    private Map<Long, List<MembershipDiscountsHistory>> loadProductMembershipDiscounts(Set<Long> nonMembershipProductIds) {
        List<FindLatestMembershipDiscountsByProductIdsRsp> membershipDiscounts =
                soapClient.getProductOMSEndpoint().findLatestMembershipDiscountsByProductIds(ContextWrapper.get(),
                        new ArrayList<>(nonMembershipProductIds));
        return CollectionUtils.emptyIfNull(membershipDiscounts).stream().collect(
                toMap(FindLatestMembershipDiscountsByProductIdsRsp::getProductId,
                        FindLatestMembershipDiscountsByProductIdsRsp::getHistories));
    }

    @Lookup
    public CartItemMembershipPricer getCartItemMembershipPricer(MembershipDiscountContext context) {
        return null;
    }
}
