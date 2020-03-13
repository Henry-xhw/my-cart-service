package com.active.services.cart.service.common;

import com.active.services.cart.client.rest.ProductService;
import com.active.services.product.nextgen.v1.dto.DiscountUsage;
import com.active.services.product.nextgen.v1.req.GetDiscountUsageReq;
import com.active.services.product.nextgen.v1.req.UpdateDiscountUsageReq;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author vpan1
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
@RequiredArgsConstructor
public class DiscountUsageHandler {
    private final ProductService productService;

    public List<DiscountUsage> getDisCountUsageByDiscountIds(List<Long> discountIds) {
        GetDiscountUsageReq getDiscountUsageReq = new GetDiscountUsageReq();
        getDiscountUsageReq.setDiscountIds(discountIds);
        return productService.getDiscountUsages(getDiscountUsageReq).getDiscountUsages();
    }

    public void updateDisCountUsage(List<Long> discountIds, Integer usage) {
        UpdateDiscountUsageReq updateDiscountUsageReq = new UpdateDiscountUsageReq();
        updateDiscountUsageReq.setDiscountIds(discountIds);
        updateDiscountUsageReq.setUsage(usage);
        productService.updateDiscountUsages(updateDiscountUsageReq);
    }
}