package com.active.services.cart.service.validator;

import com.active.services.cart.client.soap.SOAPClient;
import com.active.services.cart.domain.Cart;
import com.active.services.cart.domain.CartItem;
import com.active.services.product.FindProductsByIdListRsp;
import com.active.services.product.api.v1.soap.ProductServiceSOAPEndPoint;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CreateCartItemsValidator.class})
public class CreateCartItemsValidatorTestCase {

    @Mock
    private SOAPClient soapClient;

    private CreateCartItemsValidator validator;

    @Test
    public void validate() throws Exception {
        Cart cart = new Cart();

        CartItem cartItem = new CartItem();
        cart.setItems(Arrays.asList(cartItem));

        validator = new CreateCartItemsValidator(cart, Arrays.asList(cartItem));
        ProductServiceSOAPEndPoint productService = spy(ProductServiceSOAPEndPoint.class);
        doReturn(productService).when(soapClient).productServiceSOAPEndPoint();
        ReflectionTestUtils.setField(validator, "soapClient", soapClient);
        doReturn(new FindProductsByIdListRsp(new ArrayList<>()))
                .when(productService).findProductsByIdList(any(), any());

        CartItemsIdentifierValidator cartItemsIdentifierValidator = spy(new CartItemsIdentifierValidator(cart,
                Arrays.asList(cartItem)));
        doNothing().when(cartItemsIdentifierValidator).validate();
        PowerMockito.whenNew(CartItemsIdentifierValidator.class).withAnyArguments()
                .thenReturn(cartItemsIdentifierValidator);

        CartItemsProductValidator cartItemsProductValidator = spy(new CartItemsProductValidator(
                Arrays.asList(cartItem), new ArrayList()));
        doNothing().when(cartItemsProductValidator).validate();
        PowerMockito.whenNew(CartItemsProductValidator.class).withAnyArguments()
                .thenReturn(cartItemsProductValidator);

        CartItemsCurrencyValidator cartItemsCurrencyValidator = spy(new CartItemsCurrencyValidator(
                cart, new ArrayList()));
        doNothing().when(cartItemsCurrencyValidator).validate();
        PowerMockito.whenNew(CartItemsCurrencyValidator.class).withAnyArguments()
                .thenReturn(cartItemsCurrencyValidator);

        validator.validate();
    }
}
