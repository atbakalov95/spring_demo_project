package com.example.demo.resourceserver.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "security.oauth2.bit-bucket-oauth2")
public class BitBucketOAuth2Properties {
    private String key;
    private String secret;
}
