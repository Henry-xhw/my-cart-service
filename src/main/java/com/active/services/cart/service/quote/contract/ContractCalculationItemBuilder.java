package com.active.services.cart.service.quote.contract;

import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;
import com.active.services.contract.controller.v1.CalculationItem;
import com.active.services.contract.controller.v1.ContractSetting;
import com.active.services.contract.controller.v1.FeeOwner;
import com.active.services.product.Product;
import com.active.services.product.ProductSaleSettings;

import org.apache.commons.lang3.builder.Builder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class ContractCalculationItemBuilder implements Builder<CalculationItem> {
    public static final BigDecimal ONE_HUNDRED_PERCENT = new BigDecimal("100.000")
            .setScale(3, BigDecimal.ROUND_HALF_UP);

    private Product product;

    private Instant businessDate;

    private CartItem cartItem;

    private boolean online;

    private FeeOwner feeOwner;

    public ContractCalculationItemBuilder product(Product product) {
        this.product = product;

        return this;
    }

    public ContractCalculationItemBuilder businessDate(Instant businessDate) {
        this.businessDate = businessDate;

        return this;
    }

    public ContractCalculationItemBuilder cartItem(CartItem cartItem) {
        this.cartItem = cartItem;

        return this;
    }

    public ContractCalculationItemBuilder online(boolean online) {
        this.online = online;

        return this;
    }

    public ContractCalculationItemBuilder feeOwner(FeeOwner feeOwner) {
        this.feeOwner = feeOwner;

        return this;
    }

    @Override
    public CalculationItem build() {
        CalculationItem item = new CalculationItem();

        if (product == null) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, "Product not found: " + cartItem.getProductId());
        }
        item.setAgencyId(product.getAgencyId());
        item.setBusinessDate(businessDate);
        item.setContractSetting(buildContractSetting());
        item.setListPrice(cartItem.getGrossPrice());
        // need to confirm surcharge in cartItem
        item.setNetPrice(cartItem.getNetPrice());
        item.setSurcharge(null);
        item.setProductType(product.getProductType());
        item.setReferenceId(cartItem.getIdentifier());
        return item;
    }

    private ContractSetting buildContractSetting() {
        ContractSetting contractSetting = new ContractSetting();
        contractSetting.setProductClassId(product.getProductClassId());
        contractSetting.setTimingContractId(product.getProductContractId());
        contractSetting.setAbsorbedPercent(getAbsorbedPercent());
        contractSetting.setFeeOverrideAmount(product.getProductSaleSettings().getFeeOverrideAmount());
        return contractSetting;
    }

    private BigDecimal getAbsorbedPercent() {
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
