package com.active.services.cart.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class BookingDuration {
    private final LocalDateTime from;
    private final LocalDateTime to;
}
