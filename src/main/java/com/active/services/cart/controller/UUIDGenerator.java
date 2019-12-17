package com.active.services.cart.controller;

import java.util.UUID;

import com.active.services.cart.domain.BaseDomainObject;
import com.active.services.cart.model.v1.BaseDto;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;

public class UUIDGenerator {

    @AfterMapping
    public void generateUUID(@MappingTarget BaseDomainObject baseDomainObject, BaseDto baseDto,
                             @Context boolean isCreate) {
        if (isCreate) {
            baseDomainObject.setIdentifier(UUID.randomUUID());
        }
    }
}
