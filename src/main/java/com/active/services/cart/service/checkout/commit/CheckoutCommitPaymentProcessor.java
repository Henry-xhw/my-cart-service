package com.active.services.cart.service.checkout.commit;

import com.active.services.ContextWrapper;
import com.active.services.TenderType;
import com.active.services.cart.common.CartException;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.domain.Payment;
import com.active.services.cart.model.CartItemFeeAllocation;
import com.active.services.cart.model.ErrorCode;
import com.active.services.cart.repository.CartRepository;
import com.active.services.cart.service.checkout.CheckoutBaseProcessor;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.cart.service.checkout.CheckoutEvent;
import com.active.services.payment.management.PaymentStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.apache.commons.collections4.CollectionUtils.emptyIfNull;

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
                .filter(item -> Objects.nonNull(item.getFees())).map(CartItem::getFlattenCartItemFees)
                .flatMap(List::stream).map(f -> f.getDueAmount().multiply(BigDecimal.valueOf(f.getUnits())))
                .reduce(BigDecimal::add).get();
        payment.setIdentifier(UUID.randomUUID());
        payment.setAmount(totalAmount);
        payment.setPaymentStatus(PaymentStatus.APPROVED);
        payment.setTenderType(TenderType.CREDIT_CARD);
        getCheckoutContext().setPayments(Arrays.asList(payment));

        //validation allocation amount and payment
        validateFeeAllocation(payment);

        // fill paymentTxId for feeAllocation
        emptyIfNull(getCheckoutContext().getFeeAllocations()).stream().forEach(feeAllocation -> {
            feeAllocation.setPaymentTxId(payment.getIdentifier());
        });

        // Should place order to be finalized if payment passed.
        cartRepository.finalizeCart(getCheckoutContext().getCart().getIdentifier(), ContextWrapper.get().getActorId());
    }

    private void validateFeeAllocation(Payment p) {
        if (CollectionUtils.isEmpty(getCheckoutContext().getFeeAllocations())) {
            BigDecimal totalDueAmount = getCheckoutContext().getTotalDueAmount();
            if (p.getAmount().compareTo(BigDecimal.ZERO) > 0 && p.getAmount().compareTo(totalDueAmount) != 0) {
                throw new CartException(ErrorCode.VALIDATION_ERROR, "Can not allocate fee as payment amounts not " +
                        "equal to due amounts.");
            }

            return;
        }

        // Allocation amount be the same as payment amounts
        BigDecimal allocatedAmounts = getCheckoutContext().getFeeAllocations().stream()
                .map(CartItemFeeAllocation::getAmount).reduce((one, two) -> one.add(two)).get();

        if (allocatedAmounts.compareTo(p.getAmount()) != 0) {
            throw new CartException(ErrorCode.VALIDATION_ERROR, "Allocated amount {} should be the same as payed " +
                    "amount {}", p.getAmount());
        }
    }

}
