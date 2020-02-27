package com.active.services.cart.service.quote.price;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.product.nextgen.v1.dto.QuoteItemDto;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;
import com.active.services.product.nextgen.v1.req.QuoteReq;
import com.active.services.product.nextgen.v1.rsp.QuoteRsp;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
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
import java.util.stream.Collectors;

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
        List<CartItem> flattenCartItems = context.getCart().getFlattenCartItems();
        Map<Long, FeeDto> feeMap = getProductFeeMap(flattenCartItems);
        flattenCartItems.forEach(cartItem -> getCartItemPricer(feeMap).quote(context, cartItem));
        context.getCart().setUnflattenItems(flattenCartItems);
    }

    private Map<Long, FeeDto> getProductFeeMap(List<CartItem> flattenCartItems) {
        Map<Integer, Long> seqProdIds = getSequenceProdIdMap(flattenCartItems);
        List<QuoteItemDto> priceItems = getPriceItems(seqProdIds);
        if (CollectionUtils.isEmpty(priceItems)) {
            return new HashMap<>();
        }
        List<FeeDto> feeDtos = getPriceFromProductService(priceItems);
        return feeDtos.stream().filter(Objects::nonNull)
                .collect(toMap(feeDto -> seqProdIds.get(feeDto.getSequence()), Function.identity()));
    }

    private Map<Integer, Long> getSequenceProdIdMap(List<CartItem> flattenCartItems) {
        List<Long> quoteProdIds =
                emptyIfNull(flattenCartItems).stream()
                        .filter(cartItem -> cartItem.getOverridePrice() == null)
                        .map(CartItem::getProductId).distinct().collect(Collectors.toList());
        return emptyIfNull(quoteProdIds).stream().collect(Collectors.toMap(id -> quoteProdIds.indexOf(id),
                Function.identity()));
    }

    private List<QuoteItemDto> getPriceItems(Map<Integer, Long> sequenceProdIdMap) {
        List<QuoteItemDto> quoteItemDtos = new ArrayList<>();
        sequenceProdIdMap.forEach((key, value) -> quoteItemDtos.add(getQuoteItemDto(key, value)));
        return quoteItemDtos;
    }

    private QuoteItemDto getQuoteItemDto(int sequence, Long prodId) {
        QuoteItemDto quoteItemDto = new QuoteItemDto();
        quoteItemDto.setSequence(sequence);
        quoteItemDto.setBusinessDate(Instant.now());
        quoteItemDto.setProductId(prodId);
        return quoteItemDto;
    }

    private List<FeeDto> getPriceFromProductService(List<QuoteItemDto> items) {
        QuoteReq quoteReq = new QuoteReq();
        quoteReq.setItems(items);
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
