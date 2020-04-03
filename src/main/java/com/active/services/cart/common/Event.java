package com.active.services.cart.common;

import com.active.services.cart.domain.BaseDomainObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Event extends BaseDomainObject {

    private static ObjectMapper objectMapper;

    private String bizIdentifier;

    private String type;

    private String payload;

    public void setPayload(Object payload) {
        try {
            this.payload = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setObjectMapper(ObjectMapper om) {
        objectMapper = om;
    }
}
