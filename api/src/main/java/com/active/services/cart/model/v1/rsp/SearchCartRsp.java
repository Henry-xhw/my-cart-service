package com.active.services.cart.model.v1.rsp;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SearchCartRsp {
    private List<UUID> cartIds;
}
