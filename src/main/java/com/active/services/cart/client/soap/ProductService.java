package com.active.services.cart.client.soap;

import com.active.services.ActiveEntityNotFoundException;
import com.active.services.cart.domain.CartItem;
import com.active.services.domain.dto.ProductDto;
import com.active.services.product.FindProductsByIdListReq;
import com.active.services.product.FindProductsByIdListRsp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.active.services.cart.util.AuditorAwareUtil.getContext;

/**
 * @author henryxu
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class ProductService {
    private final SOAPClient soapClient;

    public List<ProductDto> getProducts(List<CartItem> cartItems) {
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
