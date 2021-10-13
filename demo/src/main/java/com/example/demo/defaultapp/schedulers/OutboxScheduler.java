package com.example.demo.defaultapp.schedulers;

import com.example.demo.defaultapp.enums.MessageStatusEnum;
import com.example.demo.defaultapp.exceptions.ServiceException;
import com.example.demo.defaultapp.properties.RedisProperties;
import com.example.demo.defaultapp.services.OutboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.logging.Level;
import java.util.logging.Logger;

@Configuration
@EnableScheduling
public class OutboxScheduler {
    private static final Logger logger = Logger.getLogger(OutboxScheduler.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final RedisProperties redisProperties;
    private final OutboxService outboxService;
    private final StringRedisTemplate template;

    public OutboxScheduler(RedisProperties redisProperties, OutboxService outboxService, StringRedisTemplate template) throws ServiceException {
        this.redisProperties = redisProperties;
        this.outboxService = outboxService;
        this.template = template;
    }

    @Scheduled(cron = "${redis-mq.cron}")
    public void pollAndSendOutbox() {
        try {
            logger.log(Level.INFO, "Started polling procedure");
            outboxService.findTopNWithStatus(
                            redisProperties.getScanLimit(),
                            MessageStatusEnum.STARTED
                    ).forEach(outbox -> {
                        logger.log(Level.INFO, "Message with body: " + outbox.getMessage() + "is prepared to be sent");
                        template.convertAndSend(redisProperties.getQueue().getName(), outbox.getMessage());
                        outbox.setStatus(MessageStatusEnum.SENT);
                        outboxService.update(outbox);
                    });
        } catch (RedisConnectionFailureException e) {
            logger.log(Level.SEVERE, e.getMessage());
        }

    }


}
