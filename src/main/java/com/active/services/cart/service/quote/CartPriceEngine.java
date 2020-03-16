package com.active.services.cart.service.quote;

import com.active.services.ContextWrapper;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.service.quote.contract.CartProductProcessingFeePricer;
import com.active.services.cart.service.quote.discount.aa.CartAaDiscountPricer;
import com.active.services.cart.service.quote.discount.adhoc.CartAdHocDiscountPricer;
import com.active.services.cart.service.quote.discount.multi.CartMultiDiscountPricer;
import com.active.services.cart.service.quote.discount.processor.CartDiscountPricer;
import com.active.services.cart.service.quote.price.CartUnitPricePricer;
import com.active.services.contract.controller.v1.FeeOwner;
import com.active.services.product.DiscountType;
import com.active.services.product.Product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CartPriceEngine {
    private final SOAPClient soapClient;

    private final CartMultiDiscountPricer cartMultiDiscountPricer;

    private final CartAaDiscountPricer cartAaDiscountPricer;

    private final CartUnitPricePricer cartUnitPricePricer;

    private final CartAdHocDiscountPricer cartAdHocDiscountPricer;

    public void quote(CartQuoteContext context) {
        prepare(context);
        cartUnitPricePricer.quote(context);
        applyDiscount(context);
        getCartProductProcessingFeePricer(FeeOwner.CONSUMER).quote(context);
        //ensure aa discount is the last step.
        cartAaDiscountPricer.quote(context);
    }

    private void applyDiscount(CartQuoteContext context) {
        cartMultiDiscountPricer.quote(context);
        getDiscountPricer(DiscountType.COUPON).quote(context);
        cartAdHocDiscountPricer.quote(context);
    }

    private void prepare(CartQuoteContext context) {
        List<Product> products = soapClient.productServiceSOAPEndPoint().findProductsByProductIdList(ContextWrapper.get(),
                context.getProductIds());
        context.setProducts(products);
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
