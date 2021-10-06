package com.example.demo.resourceserver.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "redis-mq")
public class RedisProperties {
    private QueueSettings queue;
    private String cron;
    private int scanLimit;

    public int getScanLimit() {
        return scanLimit;
    }

    public void setScanLimit(int scanLimit) {
        this.scanLimit = scanLimit;
    }

    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public QueueSettings getQueue() {
        return queue;
    }

    public void setQueue(QueueSettings queue) {
        this.queue = queue;
    }

    public static class QueueSettings {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
