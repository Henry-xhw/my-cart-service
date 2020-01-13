package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.client.rest.OrderService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.infrastructure.mapper.PlaceCartMapper;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.cart.model.PaymentAccountResult;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.OrderResponseDTO;
import com.active.services.order.management.api.v3.types.PlaceOrderReq;
import com.active.services.order.management.api.v3.types.PlaceOrderRsp;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.active.services.cart.model.ErrorCode.PLACE_ORDER_ERROR;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class CheckoutCommitOrderProcessor {
    private final CheckoutContext checkoutContext;

    @Autowired
    private OrderService orderService;

    public void process() {
        OrderDTO orderDTO = PlaceCartMapper.MAPPER.toOrderDTO(checkoutContext.getCart());
        orderDTO.setOrderUrl(checkoutContext.getOrderUrl());
        orderDTO.setSendOrderReceipt(checkoutContext.isSendReceipt());
        String payAccountId = Optional.ofNullable(checkoutContext.getPaymentAccount())
                        .map(PaymentAccount::getAmsAccountId).orElse(null);
        orderDTO.setPayAccountId(payAccountId);
        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrderDTO(orderDTO);

        PlaceOrderRsp rsp = orderService.placeOrder(req);

        // TODO: exception handling
        if (CollectionUtils.isEmpty(rsp.getOrderResponses())) {
            throw new CartException(PLACE_ORDER_ERROR, "Failed to placeOrder for cart: {0}, {1}", rsp.getErrorCode(),
                    rsp.getErrorMessage());
        }

        List<CheckoutResult> checkoutResults = rsp.getOrderResponses().stream().map(OrderResponseDTO::getOrderId)
                .map(orderId -> new CheckoutResult(orderId, new PaymentAccountResult()))
                .collect(Collectors.toList());
        checkoutContext.setCheckoutResults(checkoutResults);
    }
}
