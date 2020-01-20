package com.active.services.cart.service.checkout.commit;

import com.active.services.TenderType;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.CartItemFee;
import com.active.services.cart.domain.Payment;
import com.active.services.cart.domain.PaymentStatus;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.CheckoutBaseProcessor;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.cart.service.checkout.CheckoutEvent;
import com.active.services.cart.util.AuditorAwareUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class CheckoutCommitPaymentProcessor extends CheckoutBaseProcessor {
    @Autowired
    private CartRepository cartRepository;

    public CheckoutCommitPaymentProcessor(CheckoutContext checkoutContext) {
        super(checkoutContext, CheckoutEvent.CheckoutPhase.COMMIT_PAYMENT);
    }

    @Override
    protected void doProcess() {
        Cart cart = getCheckoutContext().getCart();
        Payment payment = new Payment();
        BigDecimal totalAmount = cart.getFlattenCartItems().stream()
                .filter(item -> Objects.isNull(item.getFees())).map(CartItem::getFees)
                .flatMap(List::stream).map(CartItemFee::getDueAmount)
                .reduce(BigDecimal::add).get();
        payment.setAmount(totalAmount);
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setTenderType(TenderType.CREDIT_CARD);
        getCheckoutContext().setPayments(Arrays.asList(payment));

        // Should place order to be finalized if payment passed.
        cartRepository.finalizeCart(getCheckoutContext().getCart().getIdentifier(), AuditorAwareUtil.getAuditor());
    }
}
