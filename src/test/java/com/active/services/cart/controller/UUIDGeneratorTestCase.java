package com.active.services.cart.controller;

import com.active.services.cart.domain.BaseDomainObject;
import com.active.services.cart.model.v1.BaseDto;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UUIDGeneratorTestCase {
    private BaseDomainObject baseDomainObject;

    @Before
    public void setup() {
        this.baseDomainObject = new BaseDomainObject();
    }

    @Test
    public void testGenerateUUIDSuccess() {
        UUIDGenerator generator = new UUIDGenerator();
        generator.generateUUID(baseDomainObject, new BaseDto(), true);
        Assert.assertNotNull(baseDomainObject.getIdentifier());
    }

    @Test
    public void testGenerateUUIDFailed() {
        UUIDGenerator generator = new UUIDGenerator();
        generator.generateUUID(baseDomainObject, new BaseDto(), false);
        Assert.assertNull(baseDomainObject.getIdentifier());
    }
}
