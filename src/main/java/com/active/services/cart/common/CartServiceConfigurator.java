package com.active.services.cart.common;

import com.active.platform.filter.ContextFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CartServiceConfigurator {

    @Autowired
    public void configureObjectMapper(ObjectMapper objectMapper) {
        Event.setObjectMapper(objectMapper);
    }

    @Bean
    public EventBus eventBus() {
        return new EventBus("cart-service-event-bus");
    }

    @Bean
    public FilterRegistrationBean contextFilter() {
        FilterRegistrationBean contextFilterReg = new FilterRegistrationBean();

        contextFilterReg.setFilter(new ContextFilter());
        contextFilterReg.addUrlPatterns("/carts/*");

        return contextFilterReg;
    }
}
