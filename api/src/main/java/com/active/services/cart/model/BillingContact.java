package com.active.services.cart.model;

import com.active.services.domain.Address;
import com.active.services.domain.EmailAddress;
import com.active.services.domain.PhoneNumber;

import lombok.Data;

@Data
public class BillingContact {
    private String firstName;
    private String lastName;
    private Address address;
    private PhoneNumber phoneNumber;
    private EmailAddress emailAddress;
}
