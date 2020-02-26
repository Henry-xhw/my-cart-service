package com.active.services.cart.service.quote.price;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.util.TreeBuilder;
import com.active.services.product.nextgen.v1.dto.QuoteItemDto;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;
import com.active.services.product.nextgen.v1.req.QuoteReq;
import com.active.services.product.nextgen.v1.rsp.QuoteRsp;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.active.services.cart.model.ErrorCode.QUOTE_ERROR;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.ListUtils.emptyIfNull;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartUnitPricePricer implements CartPricer {

    private final ProductService productService;

    @Override
    public void quote(CartQuoteContext context) {
        Map<Long, FeeDto> feeDtoMap = new HashMap<>();
        List<CartItem> flattenCartItems = context.getCart().getFlattenCartItems();
        List<QuoteItemDto> notOverridePriceItems = getNotOverridePriceItems(flattenCartItems);
        if (CollectionUtils.isNotEmpty(notOverridePriceItems)) {
            QuoteReq quoteReq = new QuoteReq();
            quoteReq.setItems(notOverridePriceItems);
            List<FeeDto> feeDtos = getPriceFromProductService(quoteReq);
            feeDtoMap = buildCartItemFeeResult(notOverridePriceItems, feeDtos);
        }
        Map<Long, FeeDto> finalFeeMap = feeDtoMap;
        flattenCartItems.forEach(cartItem ->
                getCartItemPricer(finalFeeMap).quote(context, cartItem)
        );
        TreeBuilder<CartItem> baseTreeTreeBuilder = new TreeBuilder<>(flattenCartItems);
        context.getCart().setItems(baseTreeTreeBuilder.buildTree());
    }

    private Map<Long, FeeDto> buildCartItemFeeResult(List<QuoteItemDto> notOverridePriceItems, List<FeeDto> feeDtos) {
        Map<Long, FeeDto> feeDtoHashMap = new HashMap<>();
        Map<Integer, FeeDto> sequenceFeeDtoMap = emptyIfNull(feeDtos)
                .stream()
                .filter(Objects::nonNull)
                .collect(toMap(FeeDto::getSequence, Function.identity()));

        emptyIfNull(notOverridePriceItems)
                .stream()
                .filter(Objects::nonNull)
                .forEach(notOverridePriceItem ->
                        feeDtoHashMap.put(notOverridePriceItem.getProductId(),
                                sequenceFeeDtoMap.get(notOverridePriceItem.getSequence())));
        return feeDtoHashMap;
    }

    private List<QuoteItemDto> getNotOverridePriceItems(List<CartItem> flattenCartItems) {
        ArrayList<QuoteItemDto> quoteItemDtos = new ArrayList<>();
        emptyIfNull(flattenCartItems).forEach(cartItem -> {
            if (Objects.isNull(cartItem.getOverridePrice())) {
                QuoteItemDto quoteItemDto = new QuoteItemDto();
                quoteItemDto.setSequence(quoteItemDtos.size());
                quoteItemDto.setBusinessDate(Instant.now());
                quoteItemDto.setProductId(cartItem.getProductId());
                quoteItemDtos.add(quoteItemDto);
            }
        });
        return quoteItemDtos;
    }

    private List<FeeDto> getPriceFromProductService(QuoteReq quoteReq) {
        QuoteRsp result = productService.quote(quoteReq);
        if (BooleanUtils.isFalse(result.isSuccess()) || CollectionUtils.isEmpty(result.getFeeDtos())) {
            throw new CartException(QUOTE_ERROR, "Failed to quote for cart: {0}, {1}", result.getErrorCode(),
                    result.getErrorMessage());
        }
        return result.getFeeDtos();
    }

    @Lookup
    public CartItemUnitPricePricer getCartItemPricer(Map<Long, FeeDto> feeDtoMap) {
        return null;
    }
}
