package com.active.services.cart.service.quote;

import com.active.services.ContextWrapper;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.service.quote.contract.CartProductProcessingFeePricer;
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

    public void quote(CartQuoteContext context) {
        prepare(context);
        getCartUnitPricePricer().quote(context);
        applyDiscount(context);
        getCartProductProcessingFeePricer(FeeOwner.CONSUMER).quote(context);
    }

    private void applyDiscount(CartQuoteContext context) {
        getDiscountPricer(DiscountType.MULTI).quote(context);
    }

    private void prepare(CartQuoteContext context) {
        List<Product> products = soapClient.productServiceSOAPEndPoint().
                findProductsByProductIdList(ContextWrapper.get(), context.getProductIds());
        context.setProducts(products);
    }

    @Lookup
    public CartUnitPricePricer getCartUnitPricePricer() {
        return null;
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
