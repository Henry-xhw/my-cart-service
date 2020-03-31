package com.active.services.cart.service.quote.discount.aa;

import com.active.services.ContextWrapper;
import com.active.services.ProductType;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.Product;
import com.active.services.product.discount.aa.AaDiscount;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartAaDiscountPricer implements CartPricer {

    @NonNull
    private final SOAPClient soapClient;

    @Override
    public void quote(CartQuoteContext context) {

        if (context == null || context.getCart() == null) {
            return;
        }

        boolean isAaMember = context.isAaMember() || context.hasCartItemWithType(ProductType.AA_MEMBERSHIP);
        if (!isAaMember) {
            return;
        }

        AaDiscount aaDiscount = soapClient.getProductOMSEndpoint()
                .findLatestAaDiscountByCurrencyCode(ContextWrapper.get(), context.getCart().getCurrencyCode());
        if (aaDiscount == null) {
            // no aa_discount configured for this currency, return
            return;
        }

        AADiscountContext aaDiscountContext = new AADiscountContext(aaDiscount);

        List<CartItem> qualifyingCartItem =
                context.getFlattenCartItems().stream().filter(cartItem -> isQualifyingCartItem(context,
                cartItem)).collect(Collectors.toList());
        for (CartItem cartItem : qualifyingCartItem) {
            if (!aaDiscountContext.hasRemainingAmt()) {
                break;
            }
            new CartItemAaDiscountPricer(aaDiscountContext).quote(context, cartItem);
        }
    }

    private boolean isQualifyingCartItem(CartQuoteContext context, CartItem cartItem) {
        Product prod = context.getProductsMap().get(cartItem.getProductId());
        return cartItem.getActiveProcessingFeeTotal().compareTo(BigDecimal.ZERO) > 0 &&
                prod != null && prod.getProductType() == ProductType.REGISTRATION;
    }
}