package com.active.services.cart.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingDuration {
    private LocalDateTime from;
    private LocalDateTime to;
}
