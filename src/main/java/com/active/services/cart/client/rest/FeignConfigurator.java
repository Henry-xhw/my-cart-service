package com.active.services.cart.client.rest;

import com.fasterxml.jackson.databind.ObjectMapper;

import feign.Feign;
import feign.Logger;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@ConfigurationProperties(prefix = "ok-http")
@Data
@Component
@Slf4j
public class FeignConfigurator {
    private int connectTimeOut = 10;

    private int readWriteTimeOut = 10;

    private final ObjectMapper objectMapper;

    public <T> T buildService(String url, Class<T> clz) {
        okhttp3.OkHttpClient target = new okhttp3.OkHttpClient.Builder().connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .writeTimeout(readWriteTimeOut, TimeUnit.SECONDS).readTimeout(readWriteTimeOut, TimeUnit.SECONDS).build();

        Logger.Level level = LOG.isDebugEnabled() ? Logger.Level.FULL : Logger.Level.BASIC;

        return Feign.builder().encoder(new JacksonEncoder(objectMapper)).decoder(new JacksonDecoder(objectMapper))
                .client(new OkHttpClient(target)).logger(new Slf4jLogger(FeignConfigurator.class))
                .logLevel(level).target(clz, url);
    }
}
