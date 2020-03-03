package com.active.services.cart.service.checkout.commit;

import com.active.services.cart.BaseTestCase;
import com.active.services.cart.service.checkout.CheckoutContext;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class CheckoutCommitPhaseProcessorTestCase extends BaseTestCase {

    private CheckoutCommitPhaseProcessor ccphp = Mockito.mock(CheckoutCommitPhaseProcessor.class);

    private CheckoutCommitPaymentProcessor ccpp = Mockito.mock(CheckoutCommitPaymentProcessor.class);

    private CheckoutCommitInventoryProcessor ccip = Mockito.mock(CheckoutCommitInventoryProcessor.class);

    private CheckoutCommitOrderProcessor ccop = Mockito.mock(CheckoutCommitOrderProcessor.class);

    @Test
    public void processSuccess() {
        Mockito.doCallRealMethod().when(ccphp).process();
        Mockito.when(ccphp.getCheckoutPaymentProcessor(any())).thenReturn(ccpp);
        Mockito.when(ccphp.getCheckoutInventoryProcessor(any())).thenReturn(ccip);
        Mockito.when(ccphp.getCheckoutOrderProcessor(any())).thenReturn(ccop);
        doNothing().when(ccpp).process();
        doNothing().when(ccip).process();
        doNothing().when(ccop).process();
        ccphp.process();
        verify(ccpp).process();
        verify(ccip).process();
        verify(ccop).process();
    }

    @Test
    public void getCheckoutPaymentProcessorSuccess() {
        CheckoutContext checkoutContext = new CheckoutContext();
        Assert.assertNull(new CheckoutCommitPhaseProcessor(new CheckoutContext())
                .getCheckoutPaymentProcessor(checkoutContext));
    }

    @Test
    public void getCheckoutInventoryProcessorSuccess() {
        CheckoutContext checkoutContext = new CheckoutContext();
        Assert.assertNull(new CheckoutCommitPhaseProcessor(new CheckoutContext())
                .getCheckoutInventoryProcessor(checkoutContext));
    }

    @Test
    public void getCheckoutOrderProcessorSuccess() {
        CheckoutContext checkoutContext = new CheckoutContext();
        Assert.assertNull(new CheckoutCommitPhaseProcessor(new CheckoutContext())
                .getCheckoutOrderProcessor(checkoutContext));
    }
}
