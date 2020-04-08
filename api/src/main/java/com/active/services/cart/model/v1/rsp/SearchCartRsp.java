package com.active.services.cart.model.v1.rsp;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
public class SearchCartRsp extends BaseRsp {
    private List<UUID> cartIds;
}
