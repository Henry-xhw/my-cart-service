package com.active.services.cart.service.quote.discount.condition;

import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotExpiredSpecTestCase {

    @Test
    public void satisfy() {
        Instant now = Instant.now();
        Instant before = now.minusSeconds(1);
        Instant after = now.plusMillis(1);

        NotExpiredSpec spec = new NotExpiredSpec(null, null, now);
        assertTrue(spec.satisfy());

        spec = new NotExpiredSpec(before, null, now);
        assertTrue(spec.satisfy());

        spec = new NotExpiredSpec(before, after, now);
        assertTrue(spec.satisfy());

        spec = new NotExpiredSpec(null, after, now);
        assertTrue(spec.satisfy());
    }

    @Test
    public void satisfyWithEdgeCase() {
        Instant now = Instant.now();

        NotExpiredSpec spec = new NotExpiredSpec(null, now, now);
        assertTrue(spec.satisfy());

        spec = new NotExpiredSpec(now, null, now);
        assertTrue(spec.satisfy());
    }

    @Test
    public void satisfyForExpired() {

        Instant now = Instant.now();
        Instant before = now.minusSeconds(1);
        Instant after = now.plusMillis(1);

        NotExpiredSpec spec = new NotExpiredSpec(after, null, now);
        assertFalse(spec.satisfy());

        spec = new NotExpiredSpec(after, before, now);
        assertFalse(spec.satisfy());

        spec = new NotExpiredSpec(null, before, now);
        assertFalse(spec.satisfy());
    }
}
