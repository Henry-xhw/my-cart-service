package com.active.services.cart.restdocs;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

import java.util.HashMap;
import java.util.Map;

public class ApiDescriptionSnippet extends TemplatedSnippet {

    private String description;

    public ApiDescriptionSnippet(String description) {
        super("api-description", "api-description", new HashMap<>());
        this.description = description;
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        Map<String, Object> model = new HashMap<>();
        model.put("description", description);
        return model;
    }
}
