package com.active.services.cart.client.rest;

import com.active.services.cart.BaseTestCase;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.codec.ErrorDecoder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class FeignConfiguratorTestCase extends BaseTestCase {

    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void buildServiceSuccess() {
        assertNotNull(new FeignConfigurator(new ObjectMapper()).buildService("url", ReservationService.class,
                new ErrorDecoder.Default()));
    }

    @Test
    public void buildServiceWhenErrorDecoderIsNullSuccess() {
        assertNotNull(new FeignConfigurator(new ObjectMapper()).buildService("url", ReservationService.class,
                null));
        assertNotNull(new FeignConfigurator(new ObjectMapper()).buildService("url", ReservationService.class));
    }
}
