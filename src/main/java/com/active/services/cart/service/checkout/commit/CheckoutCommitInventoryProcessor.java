package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.client.rest.ReservationService;
import com.active.services.cart.service.checkout.CheckoutContext;
import com.active.services.inventory.rest.dto.ReservationCheckoutDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class CheckoutCommitInventoryProcessor {
    private final CheckoutContext checkoutContext;

    @Autowired
    private ReservationService reservationService;

    public void process() {
        ReservationCheckoutDto reservationCheckoutDto = new ReservationCheckoutDto();
        reservationCheckoutDto.setReservationGroupId(checkoutContext.getCart().getReservationId());
        reservationService.checkout(reservationCheckoutDto);
    }
}
