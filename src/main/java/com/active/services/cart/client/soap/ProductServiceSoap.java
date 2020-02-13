package com.active.services.cart.client.soap;

import com.active.services.ContextWrapper;
import com.active.services.cart.domain.CartItem;
import com.active.services.product.Product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

/**
 * @author henryxu
 * The class will wrap all soap call in product-service
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductServiceSoap {
    private final SOAPClient soapClient;

    public List<Product> getProductsByCartItems(List<CartItem> cartItems) {

        List<Long> uniqueProductIds = emptyIfNull(cartItems).stream().filter(Objects::nonNull).map(CartItem::getProductId)
                .distinct().collect(Collectors.toList());
        return soapClient.productServiceSOAPEndPoint()
                .findProductsByProductIdList(ContextWrapper.get(), uniqueProductIds);
    }
}
