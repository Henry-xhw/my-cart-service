package com.active.services.cart.client.rest;

import com.active.services.inventory.rest.dto.ReservationCheckoutDto;
import com.active.services.inventory.rest.dto.ReservationDTO;
import com.active.services.inventory.rest.dto.ReservationResultDTO;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;
import java.util.UUID;

@Headers({"Accept: application/vnd.active.inventory-service.v1+json",
        "Content-Type: application/vnd.active.inventory-service.v1+json"})
public interface ReservationService {
    @RequestLine("PUT /temp-reservations/{reservationGroupId}")
    ReservationResultDTO edit(@Param("reservationGroupId") UUID reservationGroupId, List<ReservationDTO> reservations);

    @RequestLine("POST /temp-reservations")
    ReservationResultDTO reserve(List<ReservationDTO> reservations);

    @RequestLine("POST /reservations")
    void commit(ReservationCheckoutDto reservationCheckoutDto);
}
