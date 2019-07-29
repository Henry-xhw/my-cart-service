package com.active.services.cart.infrastructure.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;


@Slf4j
@Configuration
public class CartServiceConfig {
    @Bean
    public DataSource dataSource(DataSourceProperties dataSourceProperties,
                                 HikariDataSourceProperties.OltpDataSourceProperties properties) {
        return createHikariDataSource(dataSourceProperties, properties);
    }

    private HikariDataSource createHikariDataSource(DataSourceProperties dataSourceProperties,
                                                    HikariDataSourceProperties properties) {
        HikariDataSource dataSource = dataSourceProperties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
        dataSource.setPoolName(properties.getPoolName());
        dataSource.setAutoCommit(properties.isAutoCommit());
        dataSource.setMaxLifetime(properties.getMaxLifetime());
        dataSource.setIdleTimeout(properties.getIdleTimeout());
        dataSource.setMinimumIdle(properties.getMinIdle());
        dataSource.setMaximumPoolSize(properties.getMaximumPoolSize());
        dataSource.setLeakDetectionThreshold(properties.getLeakDetectionThreshold());
        dataSource.setConnectionTimeout(properties.getConnectionTimeout());
        return dataSource;
    }
}
