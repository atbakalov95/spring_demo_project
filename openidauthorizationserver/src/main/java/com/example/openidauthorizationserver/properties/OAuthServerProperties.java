package com.example.openidauthorizationserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "user.oauth")
public class OAuthServerProperties {
    private String clientId;
    private String clientSecret;
    private String redirectUris;
}
