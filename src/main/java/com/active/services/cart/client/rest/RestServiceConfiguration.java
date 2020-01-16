package com.active.services.cart.client.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RestServiceConfiguration {
    @Value("${url.orderManagementServiceRest}")
    private String orderServiceUrl;

    @Value("${url.inventoryReservationServiceRest}")
    private String reservationServiceUrl;

    @Value("${url.productServiceRest}")
    private String productServiceUrl;

    @Autowired
    private FeignConfigurator feignConfigurator;

    @Bean
    public OrderService orderService() {
        return feignConfigurator.buildService(orderServiceUrl, OrderService.class);
    }

    @Bean
    public ReservationService reservationService() {
        return feignConfigurator.buildService(reservationServiceUrl, ReservationService.class);
    }

    @Bean
    public ProductService productService() {
        LOG.info("product rest url {}", productServiceUrl);
        return feignConfigurator.buildService(productServiceUrl, ProductService.class);
    }
}
