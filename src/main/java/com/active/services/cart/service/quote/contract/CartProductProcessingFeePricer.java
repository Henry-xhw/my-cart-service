package com.active.services.cart.service.quote.contract;

import com.active.services.cart.client.rest.ContractService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.service.quote.CartPricer;
import com.active.services.cart.service.quote.CartQuoteContext;
import com.active.services.cart.util.TreeBuilder;
import com.active.services.contract.controller.v1.CalculationItem;
import com.active.services.contract.controller.v1.FeeAmountResult;
import com.active.services.contract.controller.v1.FeeOwner;
import com.active.services.contract.controller.v1.FeeResult;
import com.active.services.contract.controller.v1.req.CalculateFeeAmountsReq;
import com.active.services.contract.controller.v1.rsp.CalculateFeeAmountsRsp;
import com.active.services.product.Product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static com.active.services.cart.model.ErrorCode.INTERNAL_ERROR;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

/**
 * @author henryxu
 */
@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartProductProcessingFeePricer implements CartPricer {

    @Autowired
    private ContractService contractService;

    private final FeeOwner feeOwner;

    @Override
    /**
     * 1. online is true in current scope, the online will be extracted into cart level or quote request? later.
     */
    public void quote(CartQuoteContext context) {

        List<CartItem> flattenCartItems = context.getCart().getFlattenCartItems();
        Instant businessDate = Instant.now();
        Map<Long, Product> foundProductById =
                emptyIfNull(context.getProducts()).stream().filter(Objects::nonNull).collect(toMap(Product::getId,
                Function.identity()));
        Map<UUID, CartItem> foundCartItemByIdentifier =
                flattenCartItems.stream().collect(toMap(CartItem::getIdentifier,
                        Function.identity()));

        List<CalculationItem> items = new ArrayList<>();
        flattenCartItems.forEach(cartItem -> {
            CalculationItem item = new ContractCalculationItemBuilder().product(foundProductById.get(cartItem.getProductId()))
                    .businessDate(businessDate).cartItem(cartItem).feeOwner(feeOwner).online(true).build();
            items.add(item);
        });

        // build CalculateFeeAmountsReq
        CalculateFeeAmountsReq req = new CalculateFeeAmountsReq();
        req.setCurrencyCode(context.getCart().getCurrencyCode());
        req.setFeeOwner(feeOwner);
        req.setOnline(true);
        req.setItems(items);

        List<FeeResult> feeResults = getProcessingFeesFromContractService(req);

        // build cartItemFees
        emptyIfNull(feeResults).stream().filter(Objects::nonNull).forEach(
                feeResult -> {
                    // get cartItem by referenceId
                    getCartItemProductProcessingFeePricer(feeResult.getFeeAmountResults())
                            .quote(context, foundCartItemByIdentifier.get(feeResult.getReferenceId()));
                }
        );

        TreeBuilder<CartItem> baseTreeTreeBuilder = new TreeBuilder<>(flattenCartItems);
        context.getCart().setItems(baseTreeTreeBuilder.buildTree());
    }

    private List<FeeResult> getProcessingFeesFromContractService(CalculateFeeAmountsReq req) {
        CalculateFeeAmountsRsp rsp = contractService.calculateFeeAmounts(req);
        if (BooleanUtils.isFalse(rsp.isSuccess()) || CollectionUtils.isEmpty(rsp.getFeeResults())) {
            throw new CartException(INTERNAL_ERROR, "Failed to quote product processing fee for cart: {0}, {1}",
                    rsp.getErrorCode(),
                    rsp.getErrorMessage());
        }
        return rsp.getFeeResults();

    }

    @Lookup
    public CartItemProductProcessingFeePricer getCartItemProductProcessingFeePricer(List<FeeAmountResult> results) {
        return null;
    }
}
