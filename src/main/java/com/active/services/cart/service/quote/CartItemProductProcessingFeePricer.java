package com.active.services.cart.service.quote;

import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.contract.controller.v1.CalculationItem;
import com.active.services.contract.controller.v1.ContractSetting;
import com.active.services.contract.controller.v1.FeeAmountResult;
import com.active.services.contract.controller.v1.FeeOwner;
import com.active.services.contract.controller.v1.type.FeeType;
import com.active.services.product.Product;
import com.active.services.product.ProductSaleSettings;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

/**
 * @author henryxu
 */
@Component
@Scope(value = SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CartItemProductProcessingFeePricer implements CartItemPricer {
    private final CartItem cartItem;
    public static final BigDecimal ONE_HUNDRED_PERCENT = new BigDecimal("100.000")
            .setScale(3, BigDecimal.ROUND_HALF_UP);

    @Override
    public void quote(CartQuoteContext context, CartItem cartItem) {
    }

    public CalculationItem buildCalculationItem(Map<Long, Product> foundProductById,
                                                 Instant businessDate
            , boolean online, FeeOwner feeOwner) {

        CalculationItem item = new CalculationItem();

        UUID referenceId = cartItem.getIdentifier();
        Product product = checkNotNull(foundProductById.get(cartItem.getProductId()), "no " +
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

    public void buildCartItemFees(List<FeeAmountResult> feeAmountResults) {

        emptyIfNull(feeAmountResults).stream().filter(Objects::nonNull).forEach(
                feeAmountResult -> {
                    // build cartItemFee
                    CartItemFee cartItemFee = new CartItemFee();
                    cartItemFee.setIdentifier(UUID.randomUUID());
                    cartItemFee.setDescription(feeAmountResult.getDescription());
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

    private ContractSetting buildContractSetting(Product product, boolean online, FeeOwner feeOwner) {
        ContractSetting contractSetting = new ContractSetting();
        contractSetting.setProductClassId(product.getProductClassId());
        contractSetting.setTimingContractId(product.getProductContractId());
        contractSetting.setAbsorbedPercent(getAbsorbedPercent(product, online, feeOwner));
        contractSetting.setFeeOverrideAmount(product.getProductSaleSettings().getFeeOverrideAmount());
        return contractSetting;
    }

    private BigDecimal getAbsorbedPercent(Product product, boolean online, FeeOwner feeOwner) {

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
}
