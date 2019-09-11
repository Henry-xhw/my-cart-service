package com.active.services.cart.model;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class BookingDuration {
    private final LocalDateTime from;
    private final LocalDateTime to;
}
