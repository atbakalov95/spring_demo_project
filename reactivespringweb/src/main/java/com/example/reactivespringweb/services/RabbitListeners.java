package com.example.reactivespringweb.services;

import com.example.reactivespringweb.properties.RabbitMQProperties;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@EnableRabbit
public class RabbitListeners {
    Logger logger = Logger.getLogger(RabbitListeners.class.getName());
    private final RabbitMQProperties rabbitMQProperties;

    public RabbitListeners(RabbitMQProperties rabbitMQProperties) {
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @RabbitListener(queues = "${rabbit-mq.queue-name}")
    public void processQueue(String message) {
        final String value = "Received from " +
                rabbitMQProperties.getQueueName() +
                " : " +
                message;
        logger.info(value);
    }

    @RabbitListener(queues = "${rabbit-mq.subscribers.subscriber1}")
    public void processSubscriber1(String message) {
        final String value = "Received from " +
                rabbitMQProperties.getExchangeName() +
                " by subscriber " +
                rabbitMQProperties.getSubscribers().getSubscriber1() +
                " : " +
                message;
        logger.info(value);
    }

    @RabbitListener(queues = "${rabbit-mq.subscribers.subscriber2}")
    public void processSubscriber2(String message) {
        final String value = "Received from " +
                rabbitMQProperties.getExchangeName() +
                " by subscriber " +
                rabbitMQProperties.getSubscribers().getSubscriber2() +
                " : " +
                message;
        logger.info(value);
    }
}
