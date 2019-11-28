package com.active.services.cart.restdocs;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

import java.util.HashMap;
import java.util.Map;

public class SimpleSnippet extends TemplatedSnippet {

    public SimpleSnippet(String snippetName, String templateName) {
        super(snippetName, templateName, new HashMap<>());
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        return new HashMap<>();
    }
}
