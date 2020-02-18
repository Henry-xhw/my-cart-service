package com.active.services.cart.client.rest;

import com.active.services.cart.common.CartException;
import com.active.services.cart.model.ErrorCode;
import com.active.services.inventory.rest.dto.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static feign.FeignException.errorStatus;

@Configuration
@Slf4j
public class RestServiceConfiguration {
    @Value("${url.orderManagementServiceRest}")
    private String orderServiceUrl;

    @Value("${url.inventoryReservationServiceRest}")
    private String reservationServiceUrl;

    @Value("${url.productServiceRest}")
    private String productServiceUrl;

    @Value("${url.contractServiceRest}")
    private String contractServiceUrl;

    @Autowired
    private FeignConfigurator feignConfigurator;

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public OrderService orderService() {
        return feignConfigurator.buildService(orderServiceUrl, OrderService.class);
    }

    @Bean
    public ReservationService reservationService() {
        return feignConfigurator.buildService(reservationServiceUrl, ReservationService.class,
                new InventoryServiceErrorDecoder());
    }

    private class InventoryServiceErrorDecoder implements ErrorDecoder {
        @Override
        public Exception decode(String methodKey, Response response) {
            FeignException exception = errorStatus(methodKey, response);
            try {
                ErrorResponse rsp = objectMapper.readValue(exception.contentUTF8(), ErrorResponse.class);
                if ("INVENTORY_NOT_AVAILABLE".equals(rsp.getErrorCode())) {
                    return new CartException(ErrorCode.OUT_OF_INVENTORY, rsp.getMessage());
                } else {
                    return exception;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Bean
    public ProductService productService() {
        LOG.info("product rest url {}", productServiceUrl);
        return feignConfigurator.buildService(productServiceUrl, ProductService.class);
    }

    @Bean
    public ContractService contractService() {
        LOG.info("contract rest url {}", contractServiceUrl);
        return feignConfigurator.buildService(contractServiceUrl, ContractService.class);
    }
}
