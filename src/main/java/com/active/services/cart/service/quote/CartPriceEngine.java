package com.active.services.cart.service.quote;

import com.active.services.ContextWrapper;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.service.quote.contract.CartProductProcessingFeePricer;
import com.active.services.cart.service.quote.discount.aa.CartAaDiscountPricer;
import com.active.services.cart.service.quote.discount.membership.MembershipDiscountPricer;
import com.active.services.cart.service.quote.discount.processor.CartDiscountPricer;
import com.active.services.cart.service.quote.price.CartUnitPricePricer;
import com.active.services.contract.controller.v1.FeeOwner;
import com.active.services.product.DiscountType;
import com.active.services.product.Product;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Component
@RequiredArgsConstructor
public class CartPriceEngine {
    private final SOAPClient soapClient;

    private final CartAaDiscountPricer cartAaDiscountPricer;

    private final CartUnitPricePricer cartUnitPricePricer;

    private final MembershipDiscountPricer membershipDiscountPricer;

    public void quote(CartQuoteContext context) {
        prepare(context);
        // price
        cartUnitPricePricer.quote(context);
        // membership
        applyMembershipDiscount(context);
        // coupon
        applyDiscount(context);
        // processing fee
        getCartProductProcessingFeePricer(FeeOwner.CONSUMER).quote(context);

        //ensure aa discount is the last step.
        applyAADiscount(context);
    }

    private void applyAADiscount(CartQuoteContext context) {
        if (isQualifying(context)) {
            cartAaDiscountPricer.quote(context);
        }
    }

    private void applyMembershipDiscount(CartQuoteContext context) {
        if (isQualifying(context)) {
            membershipDiscountPricer.quote(context);
        }
    }

    private void applyDiscount(CartQuoteContext context) {
        //
        getDiscountPricer(DiscountType.COUPON).quote(context);
    }

    private void prepare(CartQuoteContext context) {
        List<Product> products = soapClient.productServiceSOAPEndPoint().findProductsByProductIdList(ContextWrapper.get(),
                context.getProductIds());
        context.setProducts(products);
    }

    private boolean isQualifying(CartQuoteContext context) {
        return context != null && context.getCart() != null && isNotEmpty(context.getCart().getItems());
    }

    @Lookup
    public CartProductProcessingFeePricer getCartProductProcessingFeePricer(FeeOwner feeOwner) {
        return null;
    }

    @Lookup
    public CartDiscountPricer getDiscountPricer(DiscountType type)  {
        return null;
    }
}
