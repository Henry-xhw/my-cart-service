package com.active.services.cart.model.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseTree<T> extends BaseDto {
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long pid;
    @Valid
    private List<T> children = new ArrayList<>();
}
