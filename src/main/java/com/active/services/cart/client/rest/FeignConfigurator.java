package com.active.services.cart.client.rest;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import com.active.services.cart.client.rest.gson.DateTimeTypeAdapter;

import feign.Feign;
import feign.Logger;
import feign.gson.GsonDecoder;
import feign.gson.GsonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@ConfigurationProperties(prefix = "okHttp")
@Data
@Component
@Slf4j
public class FeignConfigurator {
    private int connectTimeOut = 10;

    private int readWriteTimeOut = 10;

    public <T> T buildService(String url, Class<T> tClass) {
        okhttp3.OkHttpClient target = new okhttp3.OkHttpClient.Builder().connectTimeout(connectTimeOut, TimeUnit.SECONDS)
                .writeTimeout(readWriteTimeOut, TimeUnit.SECONDS).readTimeout(readWriteTimeOut, TimeUnit.SECONDS).build();

        Logger.Level level = LOG.isDebugEnabled() ? Logger.Level.FULL : Logger.Level.BASIC;

        return Feign.builder().encoder(new GsonEncoder(Arrays.asList(new DateTimeTypeAdapter())))
                .decoder(new GsonDecoder())
                .client(new OkHttpClient(target))
                .logger(new Slf4jLogger(RestServiceConfiguration.class))
                .logLevel(level)
                .target(tClass, url);
    }
}
