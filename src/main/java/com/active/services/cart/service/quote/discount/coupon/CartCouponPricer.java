package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartDiscountBasePricer;
import com.active.services.product.nextgen.v1.req.GetDiscountUsageReq;
import com.active.services.product.nextgen.v1.rsp.GetDiscountUsageRsp;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CartCouponPricer extends CartDiscountBasePricer {

    @Autowired
    private ProductService productService;

    @Override
    protected void doQuote(CartQuoteContext context, List<CartItem> noneZeroItems) {
        CouponDiscountLoader loader = getCouponDiscountLoader(context, noneZeroItems);

        List<CartItemDiscounts> cartItemDiscounts = loader.loadDiscounts();

        if (CollectionUtils.isEmpty(cartItemDiscounts)) {
            return;
        }

        CouponDiscountContext couponDiscountContext = new CouponDiscountContext();
        couponDiscountContext.setCartItemDiscounts(cartItemDiscounts);

        GetDiscountUsageReq getDiscountUsageReq = new GetDiscountUsageReq();
        getDiscountUsageReq.setDiscountIds(new ArrayList<>(couponDiscountContext.getDiscountIds()));
        GetDiscountUsageRsp rsp = productService.getDiscountUsages(getDiscountUsageReq);
        couponDiscountContext.setDiscountUsages(rsp.getDiscountUsages());

        cartItemDiscounts.stream()
                .forEach(cartItemDisc -> getCartItemCouponPricer(couponDiscountContext, cartItemDisc)
                        .quote(context, cartItemDisc.getCartItem()));
    }

    @Lookup
    public CartItemCouponPricer getCartItemCouponPricer(CouponDiscountContext context,
                                                        CartItemDiscounts cartItemDiscount) {
        return null;
    }

    @Lookup
    public CouponDiscountLoader getCouponDiscountLoader(CartQuoteContext context, List<CartItem> cartItems) {
        return null;
    }
}
