package com.example.reactivespringweb.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("rabbit-mq")
public class RabbitMQProperties {
    private String host;
    private int port;
    private String queueName;
    private String exchangeName;
    private Subscribers subscribers;

    @Data
    public static class Subscribers {
        private String subscriber1;
        private String subscriber2;
    }
}
