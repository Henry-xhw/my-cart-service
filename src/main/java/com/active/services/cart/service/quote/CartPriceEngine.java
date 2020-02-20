package com.active.services.cart.service.quote;

import com.active.services.cart.client.soap.ProductServiceSoap;
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
    private final ProductServiceSoap productServiceSoap;

    public void quote(CartQuoteContext context) {

        prepare(context);

        getCartUnitPricePricer().quote(context);
        //applyDiscount(context);

        getCartProductProcessingFeePricer(FeeOwner.CONSUMER).quote(context);
        getCartDiscountPricer(DiscountType.ACTIVE_ADVANTAGE).quote(context);
    }

    private void applyDiscount(CartQuoteContext context) {
        getCartDiscountPricer(DiscountType.MEMBERSHIP).quote(context);
        getCartDiscountPricer(DiscountType.MULTI).quote(context);
        getCartDiscountPricer(DiscountType.COUPON).quote(context);
    }

    private void prepare(CartQuoteContext context) {
        List<Product> products = productServiceSoap.getProductsByIds(context.getProductIds());
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
    public CartDiscountPricer getCartDiscountPricer(DiscountType type) {
        return null;
    }
}
