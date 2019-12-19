package com.active.services.cart.model;

import com.active.services.domain.EmailAddress;

import lombok.Data;

@Data
public class BillingContact {
    private String firstName;
    private String lastName;
    private EmailAddress emailAddress;
}
