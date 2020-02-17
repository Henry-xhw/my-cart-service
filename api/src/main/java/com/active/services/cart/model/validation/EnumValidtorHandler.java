package com.active.services.cart.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author oms
 */
public class EnumValidtorHandler implements ConstraintValidator<EnumValidtor, Object> {

    private EnumValidtor enumValidtor;

    @Override
    public void initialize(EnumValidtor constraintAnnotation) {
        enumValidtor = constraintAnnotation;
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        Class<?> cls = enumValidtor.target();
        boolean ignoreEmpty = enumValidtor.ignoreEmpty();
        boolean result = false;
        if (cls.isEnum() || !ignoreEmpty) {
            Object[] objects = cls.getEnumConstants();
            for (Object obj : objects) {
                if (value.toString().equals(obj.toString())) {
                    result = true;
                    break;
                }
            }
        } else {
            result = true;
        }
        return result;
    }
}
