package com.active.services.cart.model.validation;

import com.active.platform.types.range.Range;

import java.util.Objects;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RangeValidator implements ConstraintValidator<ValidRange, Range> {

    @Override
    public boolean isValid(Range value, ConstraintValidatorContext context) {
        if (Objects.nonNull(value.getLowerInclusive()) && Objects.nonNull(value.getUpperExclusive())) {
            return value.getLowerInclusive().compareTo(value.getUpperExclusive()) <= 0;
        }
        return true;
    }
}
