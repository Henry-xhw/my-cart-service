package com.active.services.cart.model.v1.rsp;

import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class SearchCartRsp extends BaseRsp {
    private List<UUID> cartIds;
}
