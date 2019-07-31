package com.active.services.cart.infrastructure.config;

import com.active.services.product.api.v1.soap.ProductServiceSOAPEndPoint;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebServiceClient {
    @Value("${endpoint.soap.product.service.managementService}")
    private String productSvcUrl;

    @Bean
    public ProductServiceSOAPEndPoint getProductServiceSOAPEndPoint(ApplicationContext appContext) {
        return ServiceProxyFactory.buildClientService(appContext, productSvcUrl, ProductServiceSOAPEndPoint.class);
    }
}
