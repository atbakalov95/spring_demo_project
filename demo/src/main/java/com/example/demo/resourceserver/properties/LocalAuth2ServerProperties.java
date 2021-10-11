package com.example.demo.resourceserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.oauth2.local-server")
public class LocalAuth2ServerProperties {
    private String clientId;
    private String clientSecret;
}
