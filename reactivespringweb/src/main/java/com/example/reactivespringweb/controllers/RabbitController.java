package com.example.reactivespringweb.controllers;

import com.example.reactivespringweb.properties.RabbitMQProperties;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/rabbit")
public class RabbitController {
    private final AmqpTemplate rabbitTemplate;
    private final AmqpTemplate rabbitFanoutTemplate;
    private final RabbitMQProperties rabbitMQProperties;

    public RabbitController(
            AmqpTemplate rabbitTemplate,
            AmqpTemplate rabbitFanoutTemplate,
            RabbitMQProperties rabbitMQProperties
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitFanoutTemplate = rabbitFanoutTemplate;
        this.rabbitMQProperties = rabbitMQProperties;
    }

    @GetMapping("/send")
    public Mono<String> sendMessage(@RequestParam("value") String value) {
        return Mono.just(value)
                .map(v -> {
                    rabbitTemplate.convertAndSend(rabbitMQProperties.getQueueName(), v);
                    return "Emit to queue";
                });
    }

    @GetMapping("/fanout")
    public Mono<String> fanoutMessage(@RequestParam("value") String value) {
        return Mono.just(value)
                .map(v -> {
                    rabbitFanoutTemplate.convertAndSend(rabbitMQProperties.getQueueName(), v);
                    return "Emit to queue";
                });
    }
}
