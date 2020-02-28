package com.active.services.cart.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.Instant;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
public class BaseDomainObject {
    private Long id;

    private UUID identifier;

    private Instant createdDt;

    private Instant modifiedDt;

    private String createdBy;

    private String modifiedBy;
}
