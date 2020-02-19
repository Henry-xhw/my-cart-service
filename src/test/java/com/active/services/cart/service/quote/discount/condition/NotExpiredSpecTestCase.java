package com.active.services.cart.service.quote.discount.condition;

import com.active.services.domain.DateTime;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class NotExpiredSpecTestCase {

    @Test
    public void satisfy() {
        DateTime now = new DateTime(LocalDateTime.now());
        DateTime before = new DateTime(LocalDateTime.now().minusMinutes(1));
        DateTime after = new DateTime(LocalDateTime.now().plusMinutes(1));

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
    public void satisfyForExpired() {

        DateTime now = new DateTime(LocalDateTime.now());
        DateTime before = new DateTime(LocalDateTime.now().minusMinutes(1));
        DateTime after = new DateTime(LocalDateTime.now().plusMinutes(1));

        NotExpiredSpec spec = new NotExpiredSpec(after, null, now);
        assertFalse(spec.satisfy());

        spec = new NotExpiredSpec(after, before, now);
        assertFalse(spec.satisfy());

        spec = new NotExpiredSpec(null, before, now);
        assertFalse(spec.satisfy());
    }
}
