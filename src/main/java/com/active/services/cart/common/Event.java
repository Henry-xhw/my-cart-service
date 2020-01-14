package com.active.services.cart.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Event {

    static ObjectMapper objectMapper;

    private String identifier;

    private String type;

    private String payload;

    public void setPayload(Object payload) {
        try {
            this.payload = objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
