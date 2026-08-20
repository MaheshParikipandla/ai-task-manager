package com.mahi.aitaskmanager.config;

import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfig {

    @Bean
    public Flyway flyway(DataSource dataSource) {
        // Configure Flyway programmatically so we don't rely on Spring Boot's
        // FlywayAutoConfiguration (incompatible with newer flyway-core API).
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/migration")
                .load();

        // Run migrations at startup
        flyway.migrate();
        return flyway;
    }
}
