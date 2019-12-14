package com.active.henry.spring.test.annotations.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ConfigA.class)
public class ConfigB {
    @Bean(name = "c")
    public B b() {
        return new B();
    }
}