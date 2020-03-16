package com.active.services.cart.service.quote.discount.processor;

import com.active.platform.concurrent.TaskRunner;
import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.service.quote.discount.CartItemDiscounts;
import com.active.services.cart.service.quote.discount.DiscountHandler;
import com.active.services.cart.service.quote.discount.DiscountLoader;
import com.active.services.cart.service.quote.discount.coupon.CouponDiscountHandler;
import com.active.services.cart.service.quote.discount.coupon.CouponDiscountLoader;
import com.active.services.cart.service.quote.discount.membership.MemberShipDiscountHandler;
import com.active.services.cart.service.quote.discount.membership.MembershipDiscountLoader;
import com.active.services.product.DiscountType;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

import javax.ws.rs.NotSupportedException;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartDiscountPricer implements CartPricer {

    @NonNull
    private final DiscountType type;
    @Autowired
    private SOAPClient soapClient;
    @Autowired
    private TaskRunner taskRunner;
    @Autowired
    private ProductService productService;


    @Override
    public void quote(CartQuoteContext context) {
        DiscountLoader loader = getDiscountLoader(context);
        List<CartItemDiscounts> cartItemDiscounts = loader.load();
        if (CollectionUtils.isEmpty(cartItemDiscounts)) {
            return;
        }
        if (DiscountType.MEMBERSHIP == type) {
            List<Long> newAddedMembershipIds = loadNewAddMembershipIds(loader);
            cartItemDiscounts.stream()
                    .forEach(cartItemDisc -> new CartItemDiscountPricer(getMembershipHandler(context, cartItemDisc,
                            newAddedMembershipIds))
                            .quote(context, cartItemDisc.getCartItem()));
            return;
        }
        if (DiscountType.COUPON == type) {
            List<DiscountUsage> discountUsages = loadDiscountUsage(loader, cartItemDiscounts);
            cartItemDiscounts.stream()
                    .forEach(cartItemDisc -> new CartItemDiscountPricer(getCouponCodeHandler(context, cartItemDisc,
                            discountUsages))
                            .quote(context, cartItemDisc.getCartItem()));
            return;
        }
    }

    private DiscountHandler getCouponCodeHandler(CartQuoteContext context, CartItemDiscounts cartItemDisc, List<DiscountUsage> discountUsages) {
        return new CouponDiscountHandler(context, cartItemDisc, discountUsages);
    }

    private DiscountHandler getMembershipHandler(CartQuoteContext context,
                                                 CartItemDiscounts cartItemDisc, List<Long> loadNewAddMembershipIds) {
        return new MemberShipDiscountHandler(context, cartItemDisc, loadNewAddMembershipIds);
    }


    private List<DiscountUsage> loadDiscountUsage(DiscountLoader loader, List<CartItemDiscounts> cartItemDiscounts) {
        return ((CouponDiscountLoader) loader).loadDiscountUsage(cartItemDiscounts);
    }

    private List<Long> loadNewAddMembershipIds(DiscountLoader loader) {
        return ((MembershipDiscountLoader) loader).loadNewItemMembershipIds();
    }


    private DiscountLoader getDiscountLoader(CartQuoteContext context) {
        if (DiscountType.COUPON == type) {
            return CouponDiscountLoader.builder().context(context)
                    .soapClient(soapClient).taskRunner(taskRunner).productService(productService).build();
        }
        if (DiscountType.MEMBERSHIP == type) {
            return MembershipDiscountLoader.builder().context(context)
                    .soapClient(soapClient).build();
        }
        throw new NotSupportedException();
    }

}
