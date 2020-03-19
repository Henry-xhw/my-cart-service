package com.active.services.cart.service.quote;

import com.active.services.ContextWrapper;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.service.quote.contract.CartProductProcessingFeePricer;
import com.active.services.cart.service.quote.discount.aa.CartAaDiscountPricer;
import com.active.services.cart.service.quote.discount.adhoc.CartAdHocDiscountPricer;
import com.active.services.cart.service.quote.discount.coupon.CartCouponPricer;
import com.active.services.cart.service.quote.discount.membership.CartMembershipPricer;
import com.active.services.cart.service.quote.discount.multi.CartMultiDiscountPricer;
import com.active.services.cart.service.quote.price.CartUnitPricePricer;
import com.active.services.contract.controller.v1.FeeOwner;
import com.active.services.product.Product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartPriceEngine {
    @Autowired
    private SOAPClient soapClient;

    @Autowired
    private CartUnitPricePricer cartUnitPricePricer;

    @Autowired
    private CartMultiDiscountPricer cartMultiDiscountPricer;

    @Autowired
    private CartCouponPricer cartCouponPricer;

    @Autowired
    private CartMembershipPricer cartMembershipPricer;

    @Autowired
    private CartAaDiscountPricer cartAaDiscountPricer;

    @Autowired
    private CartAdHocDiscountPricer cartAdHocDiscountPricer;

    public void quote(CartQuoteContext context) {
        prepare(context);
        Arrays.asList(cartUnitPricePricer,
                cartMembershipPricer,
                cartMultiDiscountPricer,
                cartCouponPricer,
                cartAdHocDiscountPricer,
                getCartProductProcessingFeePricer(FeeOwner.CONSUMER),
                cartAaDiscountPricer)
                .forEach(cartPricer -> cartPricer.quote(context));
    }

    private void prepare(CartQuoteContext context) {
        List<Product> products = soapClient.productServiceSOAPEndPoint().findProductsByProductIdList(
                ContextWrapper.get(), context.getProductIds());
        context.setProducts(products);
    }

    @Lookup
    public CartProductProcessingFeePricer getCartProductProcessingFeePricer(FeeOwner feeOwner) {
        return null;
    }
}
