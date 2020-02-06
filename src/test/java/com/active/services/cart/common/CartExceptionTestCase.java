package com.active.services.cart.common;

import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartDataFactory;
import com.active.services.cart.domain.CartItem;
import com.active.services.cart.model.ErrorCode;

import org.junit.Assert;
import org.junit.Test;

import java.util.Optional;
import java.util.UUID;

public class CartExceptionTestCase {

    @Test
    public void testCartExceptionSuccess(){
        CartException exception = new CartException(ErrorCode.VALIDATION_ERROR, "validtion error");
        Assert.assertEquals(ErrorCode.VALIDATION_ERROR, exception.getErrorCode());
        Assert.assertEquals("validtion error", exception.getErrorMessage());

        CartException exception1 = new CartException(new Exception("cause"),
                ErrorCode.CART_NOT_FOUND, "cart not found");
        Assert.assertEquals("cause", exception1.getCause().getMessage());
        Assert.assertEquals(ErrorCode.CART_NOT_FOUND, exception1.getErrorCode());
        Assert.assertEquals("cart not found", exception1.getErrorMessage());
    }
}
