package com.active.services.cart.model.v1;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = false)
public class QuoteCartDto extends BaseDto {

    @NotNull
    private UUID ownerId;

    @NotNull
    private UUID keyerId;

    @NotNull
    private String currencyCode;

    @Valid
    private List<QuoteCartItemDto> items = new ArrayList<>();
}
