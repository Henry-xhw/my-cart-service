package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.client.rest.ReservationService;
import com.active.services.cart.common.CartException;
import com.active.services.cart.service.checkout.CheckoutBaseProcessor;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.inventory.rest.dto.ReservationCheckoutDto;
import com.active.services.inventory.rest.dto.ReservationCheckoutResultDTO;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import static com.active.services.cart.model.ErrorCode.INTERNAL_ERROR;
import static com.active.services.cart.service.checkout.CheckoutEvent.CheckoutPhase.COMMIT_INVENTORY;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Slf4j
public class CheckoutCommitInventoryProcessor extends CheckoutBaseProcessor {
    @Autowired
    private ReservationService reservationService;

    public CheckoutCommitInventoryProcessor(CheckoutContext checkoutContext) {
        super(checkoutContext, COMMIT_INVENTORY);
    }

    @Override
    protected void doProcess() {
        ReservationCheckoutDto reservationCheckoutDto = new ReservationCheckoutDto();
        reservationCheckoutDto.setReservationGroupId(checkoutContext.getCart().getReservationId());
        ReservationCheckoutResultDTO reservationCheckoutResultDto = reservationService.checkout(reservationCheckoutDto);
        if (reservationCheckoutResultDto.getIsSucceed() == null || !reservationCheckoutResultDto.getIsSucceed()) {
            publishFailedEvent();
            throw new CartException(INTERNAL_ERROR, "Failed to commit inventory");
        }
    }
}
