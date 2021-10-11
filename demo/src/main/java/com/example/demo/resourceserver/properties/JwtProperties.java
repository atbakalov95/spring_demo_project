package com.example.demo.resourceserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    private String header;
    private String secret;
    private long expirationSeconds;
}
