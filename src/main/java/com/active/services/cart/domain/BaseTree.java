package com.active.services.cart.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BaseTree<T extends BaseTree> extends BaseDomainObject {

    private Long parentId;

    private List<T> subItems = new ArrayList<>();
}
