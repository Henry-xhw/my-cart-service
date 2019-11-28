package com.active.services.cart.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;

@AutoConfigureRestDocs
public class BaseControllerTestCase {

    protected static final String ACTOR_ID = "Actor-Id";

    protected static final String ACTOR_EMAIL = "cart.service.ut@activenetwork.com";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected HttpHeaders actorIdHeader() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(ACTOR_ID, ACTOR_EMAIL);
        return httpHeaders;
    }
}
