package com.active.services.cart.model.v1.req;

import com.active.services.cart.model.MembershipMetaData;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

import javax.validation.Valid;

@Getter
@Setter
public class QuoteReq {
    private boolean aaMember;

    @Valid
    private List<MembershipMetaData> membershipMetas;
}
