package com.active.services.cart.model.v1.req;

import com.active.services.cart.model.v1.UpdateCartItemDto;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateCartItemReq {
    @NotEmpty
    @Valid
    private List<UpdateCartItemDto> items = new ArrayList<>();
}
