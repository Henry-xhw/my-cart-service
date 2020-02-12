package com.active.services.cart.controller;

import com.active.services.cart.domain.BaseDomainObject;
import com.active.services.cart.model.v1.BaseDto;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.MappingTarget;

import java.util.UUID;

public class UUIDGenerator {

    @AfterMapping
    public void generateUUID(@MappingTarget BaseDomainObject baseDomainObject, BaseDto baseDto,
                             @Context boolean isCreate) {
        if (isCreate) {
            baseDomainObject.setIdentifier(UUID.randomUUID());
        }
    }
}
