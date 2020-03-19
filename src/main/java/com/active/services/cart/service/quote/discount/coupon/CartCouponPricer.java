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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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

        // Sort CartItemDiscounts by cartItem net price in reverse order.
        //
        // For example, given the following 3 cart items and percent MOST_EXPENSIVE discount 20% <br>
        // cart item 1 = 100 <br>
        // cart item 2 = 200 <br>
        // cart item 3 = 80 <br>
        // the discount will only apply for cart item 2. cart item discount fee amount = 40.
        cartItemCoupons = cartItemCoupons.keySet().stream()
                .sorted(Comparator.comparing(CartItem::getNetAmount).reversed())
                .collect(Collectors.toMap(Function.identity(), cartItemCoupons::get, (e1, e2) -> e1,
                        LinkedHashMap::new));

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
