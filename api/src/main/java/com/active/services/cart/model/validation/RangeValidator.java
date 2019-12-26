package com.active.services.cart.model.validation;

import com.active.services.cart.model.Range;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeValidator implements ConstraintValidator<ValidRange, Range> {

    @Override
    public boolean isValid(Range value, ConstraintValidatorContext context) {
        if (Objects.nonNull(value.getLower()) && Objects.nonNull(value.getUpper())) {
            return value.getLower().compareTo(value.getUpper()) <= 0;
        }
        return true;
    }
}
