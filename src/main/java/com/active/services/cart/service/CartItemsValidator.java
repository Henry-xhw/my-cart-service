package com.active.services.cart.service;

import com.active.services.ActiveEntityNotFoundException;
import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
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
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.active.services.cart.domain.Cart.flattenCartItems;
import static com.active.services.cart.util.AuditorAwareUtil.getContext;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class CartItemsValidator {

    private final Cart cart;

    private final List<CartItem> cartItems;

    @Autowired
    private SOAPClient soapClient;

    public CartItemsValidator(Cart cart, List<CartItem> cartItems) {
        this.cart = cart;
        this.cartItems = flattenCartItems(cartItems);
    }

    public void validate() {
        if (CollectionUtils.isEmpty(cartItems)) {
            return;
        }

        List<ProductDto> foundProducts = getProducts();
        validateCartItemIdentifier();
        validateProduct(foundProducts);
        validateCurrency(foundProducts);
    }

    private void validateCartItemIdentifier() {
        boolean anyIdentifierNotFound = false;
        StringBuilder msg = new StringBuilder();

        for (CartItem it : cartItems) {
            if (it.getIdentifier() != null) {
                if (!cart.findCartItem(it.getIdentifier()).isPresent()) {
                    anyIdentifierNotFound = true;
                    msg.append("cart item - ");
                    msg.append(it.getIdentifier());
                    msg.append("does not exist.");
                }
            }
        }
        if (anyIdentifierNotFound) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, msg.toString());
        }
    }

    private void validateProduct(List<ProductDto> foundProducts) {
        Map<Long, ProductDto> foundProductById = foundProducts.stream()
                .collect(toMap(ProductDto::getId, Function.identity()));

        StringBuilder msg = new StringBuilder();
        boolean anyProductNotFound = false;
        for (CartItem cartItem : cartItems) {
            if (!foundProductById.containsKey(cartItem.getProductId())) {
                anyProductNotFound = true;
                msg.append(cartItem.getProductId());
            }
        }

        if (anyProductNotFound) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, "Product not fund: " + msg.toString());
        }
    }

    private void validateCurrency(List<ProductDto> foundProducts) {
        Map<String, List<ProductDto>> productsByCurrency = foundProducts.stream()
                .collect(groupingBy(ProductDto::getCurrency));
        productsByCurrency.remove(cart.getCurrencyCode());
        if (!productsByCurrency.isEmpty()) {
            StringBuilder msg = new StringBuilder();
            msg.append("Cart item currency code error: cart currency is - ");
            msg.append(cart.getCurrencyCode());
            msg.append(". Cart item currency -");
            productsByCurrency.forEach((productCurrency, products) -> {
                msg.append("[currency code -");
                msg.append(productCurrency);
                msg.append(", product ids -");
                msg.append(products.stream().map(p -> p.getId().toString()).collect(joining(",")));
                msg.append("]");
            });

            throw new CartException(ErrorCode.VALIDATION_ERROR, msg.toString());
        }
    }

    private List<ProductDto> getProducts() {
        List<Long> uniqueProductIds = cartItems.stream().map(CartItem::getProductId)
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
