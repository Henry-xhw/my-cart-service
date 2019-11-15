package com.active.services.cart.domain;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class BaseDomainObject {
    private UUID identifier;

    private Instant createdDt;

    private Instant modifiedDt;

    private String createdBy;

    private String modifiedBy;
}
