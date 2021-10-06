package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.PostgreSQLContainer;

@Configuration
public class TestContainersConfig {
    @Bean
    public PostgreSQLContainer postgreSQLContainer() {
        var container = new PostgreSQLContainer("postgres:13")
                .withDatabaseName("test-db")
                .withUsername("sa")
                .withPassword("sa");
        container.start();

        return container;
    }
}
