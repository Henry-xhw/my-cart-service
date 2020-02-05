package com.active.services.cart.service.quote;

import com.active.services.cart.client.rest.ContractService;
import com.active.services.cart.client.soap.ProductServiceSoap;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.cart.util.TreeBuilder;
import com.active.services.contract.controller.v1.CalculationItem;
import com.active.services.contract.controller.v1.ContractSetting;
import com.active.services.contract.controller.v1.FeeAmountResult;
import com.active.services.contract.controller.v1.FeeOwner;
import com.active.services.contract.controller.v1.FeeResult;
import com.active.services.contract.controller.v1.req.CalculateFeeAmountsReq;
import com.active.services.contract.controller.v1.rsp.CalculateFeeAmountsRsp;
import com.active.services.contract.controller.v1.type.FeeType;
import com.active.services.domain.dto.ProductDto;
import com.active.services.product.ProductSaleSettings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

import static com.active.services.cart.model.ErrorCode.INTERNAL_ERROR;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

@Component
@Slf4j
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class ProductProcessingFeePricer implements ProcessingFeePricer {
    private final ContractService contractService;
    private final ProductServiceSoap productServiceSoap;
    public static final BigDecimal ONE_HUNDRED_PERCENT = new BigDecimal("100.000")
            .setScale(3, BigDecimal.ROUND_HALF_UP);

    @Override
    public void quote(CartQuoteContext context, FeeOwner feeOwner) {
        List<CartItem> flattenCartItems = context.getCart().getFlattenCartItems();
        // add online property into cart?
        boolean online = true;
        Instant businessDate = Instant.now();

        // get products by cartItems
        List<ProductDto> productDtos = productServiceSoap.getProducts(flattenCartItems);

        Map<Long, ProductDto> foundProductById =
                emptyIfNull(productDtos).stream().filter(Objects::nonNull).collect(toMap(ProductDto::getId,
                Function.identity()));

        Map<UUID, CartItem> foundCartItemByIdentifier =
                flattenCartItems.stream().collect(toMap(CartItem::getIdentifier,
                        Function.identity()));
        List<CalculationItem> items = new ArrayList<>();

        flattenCartItems.forEach(cartItem -> {
            CalculationItem item = buildCalculationItem(cartItem, foundProductById, businessDate, online, feeOwner);
            items.add(item);
        });

        // build CalculateFeeAmountsReq
        CalculateFeeAmountsReq req = new CalculateFeeAmountsReq();
        req.setCurrencyCode(context.getCart().getCurrencyCode());
        req.setFeeOwner(feeOwner);
        req.setOnline(online);
        req.setItems(items);
        List<FeeResult> feeResults = getProcessingFeesFromContractService(req);

        // build cartItemFees
        emptyIfNull(feeResults).stream().filter(Objects::nonNull).forEach(
                feeResult -> {
                    // get cartItem by referenceId
                    CartItem cartItem = foundCartItemByIdentifier.get(feeResult.getReferenceId());
                    buildCartItemFees(feeResult.getFeeAmountResults(), cartItem);
                }
        );

        TreeBuilder<CartItem> baseTreeTreeBuilder = new TreeBuilder<>(flattenCartItems);
        context.getCart().setItems(baseTreeTreeBuilder.buildTree());
    }

    private void buildCartItemFees(List<FeeAmountResult> feeAmountResults,
                                   CartItem cartItem) {

        emptyIfNull(feeAmountResults).stream().filter(Objects::nonNull).forEach(
                feeAmountResult -> {
                    // build cartItemFee
                    CartItemFee cartItemFee = new CartItemFee();
                    cartItemFee.setIdentifier(UUID.randomUUID());
                    cartItemFee.setDescription(feeAmountResult.getDescription());
                    // need to confirm
                    cartItemFee.setTransactionType(FeeTransactionType.DEBIT);
                    cartItemFee.setUnitPrice(feeAmountResult.getAmount());
                    cartItemFee.setUnits(cartItem.getQuantity());
                    if (feeAmountResult.getFeeType() == FeeType.PERCENT) {
                        cartItemFee.setType(CartItemFeeType.PROCESSING_PERCENT);
                    } else {
                        cartItemFee.setType(CartItemFeeType.PROCESSING_FLAT);
                    }
                    cartItem.getFees().add(cartItemFee);
                }
        );

    }

    private CalculationItem buildCalculationItem(CartItem cartItem, Map<Long, ProductDto> foundProductById,
                                             Instant businessDate
            , boolean online, FeeOwner feeOwner) {

        CalculationItem item = new CalculationItem();

        UUID referenceId = cartItem.getIdentifier();
        ProductDto product = checkNotNull(foundProductById.get(cartItem.getProductId()), "no " +
                "found product by productId:" +
                " " + cartItem.getProductId() + "; cartItem's identifier: " + referenceId);
        item.setAgencyId(product.getAgencyId());
        item.setBusinessDate(businessDate);
        item.setContractSetting(buildContractSetting(product, online, feeOwner));
        item.setListPrice(cartItem.getGrossPrice());
        item.setNetPrice(cartItem.getNetPrice());
        // need to confirm surcharge in cartItem
        item.setSurcharge(null);
        item.setProductType(product.getProductType());
        item.setReferenceId(referenceId);
        return item;
    }

    private ContractSetting buildContractSetting(ProductDto product, boolean online, FeeOwner feeOwner) {
        ContractSetting contractSetting = new ContractSetting();
        contractSetting.setProductClassId(product.getProductClassId());
        contractSetting.setTimingContractId(product.getProductContractId());
        contractSetting.setAbsorbedPercent(getAbsorbedPercent(product, online, feeOwner));
        contractSetting.setFeeOverrideAmount(product.getProductSaleSettings().getFeeOverrideAmount());
        return contractSetting;
    }

    private BigDecimal getAbsorbedPercent(ProductDto product, boolean online, FeeOwner feeOwner) {

        ProductSaleSettings productSaleSettings = product.getProductSaleSettings();
        if (productSaleSettings == null) {
            return null;
        }
        BigDecimal absorbedPercent = null;

        BigDecimal consumerAbsorbedPercent = online ? productSaleSettings.getConsumerAbsorbedOnlinePercent() :
                productSaleSettings.getConsumerAbsorbedOfflinePercent();
        if (feeOwner == FeeOwner.CONSUMER) {
            absorbedPercent = consumerAbsorbedPercent;
        } else {
            absorbedPercent = consumerAbsorbedPercent != null ?
                    ONE_HUNDRED_PERCENT.subtract(consumerAbsorbedPercent) : null;
        }
        return absorbedPercent;
    }

    private List<FeeResult> getProcessingFeesFromContractService(CalculateFeeAmountsReq req) {
        CalculateFeeAmountsRsp rsp = contractService.calculateFeeAmounts(req);
        if (BooleanUtils.isFalse(rsp.isSuccess()) || CollectionUtils.isEmpty(rsp.getFeeResults())) {
            throw new CartException(INTERNAL_ERROR, "Failed to quote for cart: {0}, {1}", rsp.getErrorCode(),
                    rsp.getErrorMessage());
        }
        return rsp.getFeeResults();

    }
}
