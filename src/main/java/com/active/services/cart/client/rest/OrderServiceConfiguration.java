package com.active.services.cart.client.rest;

import com.active.services.cart.client.rest.gson.DateTimeTypeAdapter;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
public class OrderServiceConfiguration {
    @Value("${url.orderManagementServiceRest}")
    private String orderServiceUrl;

    @Value("${order.okHttp.connectTimeout}")
    private int connectTimeOut = 10;

    @Value("${order.okHttp.readWriteTimeout}")
    private int readWriteTimeOut = 10;

    @Bean
    public OrderService orderService() {
        okhttp3.OkHttpClient target = new okhttp3.OkHttpClient.Builder().connectTimeout(connectTimeOut, TimeUnit.SECONDS)
            .writeTimeout(readWriteTimeOut, TimeUnit.SECONDS).readTimeout(readWriteTimeOut, TimeUnit.SECONDS).build();

        return Feign.builder().encoder(new GsonEncoder(Arrays.asList(new DateTimeTypeAdapter()))).decoder(new GsonDecoder())
            .client(new OkHttpClient(target)).logger(new Slf4jLogger(OrderServiceConfiguration.class))
            .logLevel(Logger.Level.FULL).target(OrderService.class, orderServiceUrl);
    }
}
