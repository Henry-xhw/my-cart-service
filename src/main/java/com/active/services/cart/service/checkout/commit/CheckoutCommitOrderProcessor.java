package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.client.rest.OrderService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.model.PaymentAccount;
import com.active.services.cart.model.PaymentAccountResult;
import com.active.services.cart.model.v1.CheckoutResult;
import com.active.services.cart.service.checkout.CheckoutBaseProcessor;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.cart.service.checkout.CheckoutEvent;
import com.active.services.cart.service.checkout.PaymentMapper;
import com.active.services.cart.service.checkout.PlaceCartMapper;
import com.active.services.order.management.api.v3.types.OrderDTO;
import com.active.services.order.management.api.v3.types.OrderResponseDTO;
import com.active.services.order.management.api.v3.types.PaymentDTO;
import com.active.services.order.management.api.v3.types.PlaceOrderReq;
import com.active.services.order.management.api.v3.types.PlaceOrderRsp;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.active.services.cart.model.ErrorCode.INTERNAL_ERROR;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class CheckoutCommitOrderProcessor extends CheckoutBaseProcessor {
    @Autowired
    private OrderService orderService;

    public CheckoutCommitOrderProcessor(CheckoutContext checkoutContext) {
        super(checkoutContext, CheckoutEvent.CheckoutPhase.COMMIT_ORDER);
    }

    @Override
    protected void doProcess() {
        CheckoutContext ctx = getCheckoutContext();

        OrderDTO orderDTO = PlaceCartMapper.MAPPER.toOrderDTO(ctx.getCart());
        orderDTO.setOrderUrl(ctx.getOrderUrl());
        orderDTO.setSendOrderReceipt(ctx.isSendReceipt());
        String payAccountId = Optional.ofNullable(ctx.getPaymentAccount())
                        .map(PaymentAccount::getAmsAccountId).orElse(null);
        orderDTO.setPayAccountId(payAccountId);

        List<PaymentDTO> payments = PaymentMapper.MAPPER.convert(ctx.getPayments());

        PlaceOrderReq req = new PlaceOrderReq();
        req.setOrderDTO(orderDTO);
        req.setPayments(payments);

        PlaceOrderRsp rsp = orderService.placeOrder(req);

        if (CollectionUtils.isEmpty(rsp.getOrderResponses()) || !rsp.getSuccess()) {
            publishFailedEvent();

            throw new CartException(INTERNAL_ERROR, "Failed to placeOrder for cart: {0}, {1}", rsp.getErrorCode(),
                    rsp.getErrorMessage());
        }

        List<CheckoutResult> checkoutResults = rsp.getOrderResponses().stream().map(OrderResponseDTO::getOrderId)
                .map(orderId -> new CheckoutResult(orderId, new PaymentAccountResult()))
                .collect(Collectors.toList());

        ctx.setCheckoutResults(checkoutResults);
    }
}
