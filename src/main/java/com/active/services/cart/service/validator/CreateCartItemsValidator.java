package com.active.services.cart.service.validator;

import com.active.services.ActiveEntityNotFoundException;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.domain.dto.ProductDto;
import com.active.services.product.FindProductsByIdListReq;
import com.active.services.product.FindProductsByIdListRsp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.active.services.cart.domain.Cart.flattenCartItems;
import static com.active.services.cart.util.AuditorAwareUtil.getContext;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CreateCartItemsValidator {

    private final Cart cart;

    private final List<CartItem> cartItems;

    @Autowired
    private SOAPClient soapClient;

    public CreateCartItemsValidator(Cart cart, List<CartItem> cartItems) {
        this.cart = cart;
        this.cartItems = flattenCartItems(cartItems);
    }

    public void validate() {
        if (CollectionUtils.isEmpty(cartItems)) {
            return;
        }

        new CartItemsIdentifierValidator(cart, cartItems).validate();

        List<CartItem> newCartItems = cartItems.stream().filter(item -> item.getIdentifier() == null)
                .collect(Collectors.toList());
        if (!newCartItems.isEmpty()) {
            List<ProductDto> foundProducts = getProducts(newCartItems);
            new CartItemsProductValidator(newCartItems, foundProducts).validate();
            new CartItemsCurrencyValidator(cart, foundProducts).validate();
        }
    }

    private List<ProductDto> getProducts(List<CartItem> newCartItems) {
        List<Long> uniqueProductIds = newCartItems.stream().map(CartItem::getProductId)
                .distinct().collect(Collectors.toList());

        try {
            FindProductsByIdListReq req = new FindProductsByIdListReq();
            req.setProductIds(uniqueProductIds);
            FindProductsByIdListRsp rsp = soapClient.productServiceSOAPEndPoint()
                    .findProductsByIdList(getContext(), req);

            return rsp.getProducts();
        } catch (ActiveEntityNotFoundException e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

}
