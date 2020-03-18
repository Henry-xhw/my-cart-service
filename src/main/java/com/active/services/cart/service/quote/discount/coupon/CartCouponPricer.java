package com.active.services.cart.service.quote.discount.coupon;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartDiscountBasePricer;
import com.active.services.product.Discount;
import com.active.services.product.nextgen.v1.req.GetDiscountUsageReq;
import com.active.services.product.nextgen.v1.rsp.GetDiscountUsageRsp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class CartCouponPricer extends CartDiscountBasePricer {

    @Autowired
    private ProductService productService;

    @Override
    protected void doQuote(CartQuoteContext context, List<CartItem> noneZeroItems) {

        CouponDiscountLoader loader = getCouponDiscountLoader(context, noneZeroItems);
        Map<CartItem, List<Discount>> cartItemCoupons = loader.loadDiscounts();
        if (cartItemCoupons.isEmpty()) {
            return;
        }

        CouponDiscountContext couponDiscountContext = new CouponDiscountContext();
        couponDiscountContext.setCartItemDiscountMap(cartItemCoupons);

        Set<Long> limitedDiscountIds = couponDiscountContext.getLimitedDiscountIds();
        if (!limitedDiscountIds.isEmpty()) {
            GetDiscountUsageReq getDiscountUsageReq = new GetDiscountUsageReq();
            getDiscountUsageReq.setDiscountIds(new ArrayList<>(limitedDiscountIds));
            GetDiscountUsageRsp rsp = productService.getDiscountUsages(getDiscountUsageReq);
            couponDiscountContext.setDiscountUsages(rsp.getDiscountUsages());
        }

        cartItemCoupons.forEach((cartItem, cartItemDiscounts) -> getCartItemCouponPricer(couponDiscountContext,
                cartItemDiscounts).quote(context, cartItem));
    }

    @Lookup
    public CartItemCouponPricer getCartItemCouponPricer(CouponDiscountContext context,
                                                        List<Discount> cartItemDiscounts) {
        return null;
    }

    @Lookup
    public CouponDiscountLoader getCouponDiscountLoader(CartQuoteContext context, List<CartItem> cartItems) {
        return null;
    }
}
