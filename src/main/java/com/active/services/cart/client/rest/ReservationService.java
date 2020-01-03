package com.active.services.cart.client.rest;

import java.util.UUID;

import com.active.services.inventory.rest.dto.ReservationCheckoutDto;
import com.active.services.inventory.rest.dto.ReservationCheckoutResultDTO;
import com.active.services.inventory.rest.dto.TouchReservationResultDTO;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("Accept: application/vnd.active.inventory-service.v1+json")
public interface ReservationService {
    @RequestLine("PUT /reservations/checkout")
    ReservationCheckoutResultDTO checkout(ReservationCheckoutDto reservationCheckoutDto);

    @RequestLine("PUT /reservations/{id}/createdDateTime")
    TouchReservationResultDTO touchReserve(@Param("id") UUID id);
}
