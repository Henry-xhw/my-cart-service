package com.active.services.cart.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MembershipMetaData {
    @NotNull
    private String personIdentifier;
    @NotNull
    private Long membershipId;
}
