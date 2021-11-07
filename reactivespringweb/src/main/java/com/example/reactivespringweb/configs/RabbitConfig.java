package com.example.reactivespringweb.configs;

import com.example.reactivespringweb.properties.RabbitMQProperties;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.logging.Logger;

@Configuration
public class RabbitConfig {
    Logger logger = Logger.getLogger(RabbitConfig.class.getName());
    private final RabbitMQProperties rabbitMQProperties;

    public RabbitConfig(RabbitMQProperties rabbitMQProperties) {
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(
                rabbitMQProperties.getHost(),
                rabbitMQProperties.getPort()
        );
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        return new RabbitTemplate(connectionFactory());
    }

    @Bean
    public RabbitTemplate rabbitFanoutTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory());
        template.setExchange(rabbitMQProperties.getExchangeName());
        return template;
    }

    @Bean
    public Queue myQueue() {
        return new Queue(rabbitMQProperties.getQueueName());
    }

    @Bean
    public Queue myQueue1() {
        return new Queue(rabbitMQProperties.getSubscribers().getSubscriber1());
    }

    @Bean
    public Queue myQueue2() {
        return new Queue(rabbitMQProperties.getSubscribers().getSubscriber2());
    }

    @Bean
    public FanoutExchange fanoutExchangeA(){
        return new FanoutExchange(rabbitMQProperties.getExchangeName());
    }

    @Bean
    public Binding binding1(){
        return BindingBuilder.bind(myQueue1()).to(fanoutExchangeA());
    }

    @Bean
    public Binding binding2(){
        return BindingBuilder.bind(myQueue2()).to(fanoutExchangeA());
    }
}
