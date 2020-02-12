package com.active.services.cart;

import com.active.services.ContextDefaultImpl;
import com.active.services.ContextWrapper;

import org.junit.After;
import org.junit.Before;

public class BaseTestCase {

    @Before
    public void setUp() {
        ContextDefaultImpl context = new ContextDefaultImpl(null, null, null, "unit test");
        ContextWrapper.set(context);
    }

    @After
    public void tearDown() {
        ContextWrapper.destroy();
    }
}
