package com.active.services.cart.common;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.UUID;

public class EventTestCase {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        this.objectMapper = new ObjectMapper();
        Event.setObjectMapper(objectMapper);
    }

    @Test
    public void testSetPayloadSuccess() throws IOException {
        Event event = new Event();
        event.setIdentifier(UUID.randomUUID().toString());
        event.setType("object");
        event.setPayload(event);
        String result = event.getPayload();
        Event event1 = objectMapper.readValue(result, Event.class);
        Assert.assertNotNull(event1);
        Assert.assertEquals(event1.getType(), "object");
    }

    @Test(expected = RuntimeException.class)
    public void testSetPayloadFailed() {
        Event event = new Event();
        event.setPayload(this);
    }
}
