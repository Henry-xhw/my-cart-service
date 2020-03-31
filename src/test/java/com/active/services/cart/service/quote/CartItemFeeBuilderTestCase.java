package com.active.services.cart.service.quote;

import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.Discount;
import com.active.services.cart.model.CartItemFeeType;
import com.active.services.cart.model.FeeTransactionType;
import com.active.services.contract.controller.v1.FeeAmountResult;
import com.active.services.contract.controller.v1.type.FeeType;
import com.active.services.product.nextgen.v1.dto.fee.FeeDto;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class CartItemFeeBuilderTestCase {

    @Test
    public void buildOverridePriceItemFee() {
        CartItem item = CartDataFactory.cartItem();
        CartItemFee cartItemFee = CartItemFeeBuilder.buildOverridePriceItemFee(item);
        assertEquals(item.getOverridePrice(), cartItemFee.getUnitPrice());
        assertEquals(CartItemFeeType.PRICE, cartItemFee.getType());
    }

    @Test
    public void buildPriceItemFee() {
        FeeDto feeDto = new FeeDto();
        feeDto.setAmount(BigDecimal.valueOf(5));
        feeDto.setName("name");
        feeDto.setDescription("description");
        CartItemFee cartItemFee = CartItemFeeBuilder.buildPriceItemFee(1, feeDto);
        assertEquals(cartItemFee.getUnitPrice(), feeDto.getAmount());
    }

    @Test
    public void buildDiscountItemFee() {
        Discount discount = CartDataFactory.getDiscount(UUID.randomUUID());
        CartItemFee cartItemFee = CartItemFeeBuilder.buildDiscountItemFee(discount, BigDecimal.TEN, 1);
        assertEquals(discount.getName(), cartItemFee.getName());
        assertEquals(BigDecimal.TEN, cartItemFee.getUnitPrice());
    }

    @Test
    public void buildActiveFeeCartItemFeePercent() {
        FeeAmountResult feeAmountResult = new FeeAmountResult();
        feeAmountResult.setDescription("des");
        feeAmountResult.setAmount(BigDecimal.TEN);
        feeAmountResult.setFeeType(FeeType.PERCENT);
        CartItemFee cartItemFee = CartItemFeeBuilder.buildActiveFeeCartItemFee(1, feeAmountResult);
        assertEquals(feeAmountResult.getDescription(), cartItemFee.getName());
        assertEquals(BigDecimal.TEN, cartItemFee.getUnitPrice());
        assertEquals(CartItemFeeType.PROCESSING_PERCENT, cartItemFee.getType());
    }

    @Test
    public void buildActiveFeeCartItemFeeOther() {
        FeeAmountResult feeAmountResult = new FeeAmountResult();
        feeAmountResult.setDescription("des");
        feeAmountResult.setAmount(BigDecimal.TEN);
        feeAmountResult.setFeeType(FeeType.FLAT);
        CartItemFee cartItemFee = CartItemFeeBuilder.buildActiveFeeCartItemFee(1, feeAmountResult);
        assertEquals(feeAmountResult.getDescription(), cartItemFee.getName());
        assertEquals(BigDecimal.TEN, cartItemFee.getUnitPrice());
        assertEquals(CartItemFeeType.PROCESSING_FLAT, cartItemFee.getType());
    }

    @Test
    public void buildActiveFeeCartItemFlatAdjustmentMax() {
        FeeAmountResult feeAmountResult = new FeeAmountResult();
        feeAmountResult.setDescription("des");
        feeAmountResult.setAmount(BigDecimal.TEN);
        feeAmountResult.setFeeType(FeeType.FLAT_ADJUSTMENT_MAX);
        CartItemFee cartItemFee = CartItemFeeBuilder.buildActiveFeeCartItemFee(1, feeAmountResult);
        assertEquals(feeAmountResult.getDescription(), cartItemFee.getName());
        assertEquals(BigDecimal.TEN, cartItemFee.getUnitPrice());
        assertEquals(FeeTransactionType.CREDIT, cartItemFee.getTransactionType());
    }

    @Test
    public void buildActiveFeeCartItem() {
        FeeAmountResult feeAmountResult = new FeeAmountResult();
        feeAmountResult.setDescription("des");
        feeAmountResult.setAmount(BigDecimal.TEN);
        feeAmountResult.setFeeType(FeeType.FLAT_ADJUSTMENT_MIN);
        CartItemFee cartItemFee = CartItemFeeBuilder.buildActiveFeeCartItemFee(1, feeAmountResult);
        assertEquals(feeAmountResult.getDescription(), cartItemFee.getName());
        assertEquals(BigDecimal.TEN, cartItemFee.getUnitPrice());
        assertEquals(FeeTransactionType.DEBIT, cartItemFee.getTransactionType());
    }

}
