package com.active.services.cart.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
class HikariDataSourceProperties {
    private String poolName;
    private long connectionTimeout;
    private boolean autoCommit;
    private long maxLifetime;
    private long idleTimeout;
    private int minIdle = -1;
    private int maximumPoolSize = -1;
    private long leakDetectionThreshold;

    @Component
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    static class OltpDataSourceProperties extends HikariDataSourceProperties {
    }

    @Component
    @ConfigurationProperties(prefix = "spring.datasource.ods.hikari")
    static class OdsDataSourceProperties extends HikariDataSourceProperties {
    }
}
