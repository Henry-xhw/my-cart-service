package com.active.services.cart.model.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseTree extends BaseDto {
    @JsonIgnore
    private Long id;
    @JsonIgnore
    private Long pid;
    private List<BaseTree> children = new ArrayList<>();
}
