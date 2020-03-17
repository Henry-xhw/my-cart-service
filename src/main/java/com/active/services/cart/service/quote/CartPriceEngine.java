package com.active.services.cart.service.quote;

import com.active.services.ContextWrapper;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.service.quote.contract.CartProductProcessingFeePricer;
import com.active.services.cart.service.quote.discount.CartDiscountPricer;
import com.active.services.cart.service.quote.discount.aa.CartAaDiscountPricer;
import com.active.services.cart.service.quote.price.CartUnitPricePricer;
import com.active.services.contract.controller.v1.FeeOwner;
import com.active.services.product.Product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CartPriceEngine {
    private final SOAPClient soapClient;

    private final CartAaDiscountPricer cartAaDiscountPricer;

    private final CartUnitPricePricer cartUnitPricePricer;

    private final CartDiscountPricer cartDiscountPricer;

    public void quote(CartQuoteContext context) {
        prepare(context);
        Arrays.asList(cartUnitPricePricer, cartDiscountPricer, getCartProductProcessingFeePricer(FeeOwner.CONSUMER),
                cartAaDiscountPricer).forEach(cartPricer -> cartPricer.quote(context));
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
