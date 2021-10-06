package com.example.demo.resourceserver.services.implementation;

import com.example.demo.resourceserver.dao.OutboxDao;
import com.example.demo.resourceserver.enums.MessageStatusEnum;
import com.example.demo.resourceserver.exceptions.ServiceException;
import com.example.demo.resourceserver.model.Outbox;
import com.example.demo.resourceserver.services.OutboxService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class OutboxServiceImpl implements OutboxService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = Logger.getLogger(OutboxServiceImpl.class.getName());

    private final OutboxDao outboxDao;

    public OutboxServiceImpl(OutboxDao outboxDao) {
        this.outboxDao = outboxDao;
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class)
    public void persist(Object object) throws ServiceException {
        try {
            String message = objectMapper.writeValueAsString(object);
            Outbox outbox = new Outbox();
            outbox.setMessage(message);
            outbox.setStatus(MessageStatusEnum.STARTED);
            outboxDao.persist(outbox);
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional(
            propagation = Propagation.REQUIRED,
            rollbackFor = ServiceException.class)
    public UUID save(Object object) throws ServiceException {
        try {
            String message = objectMapper.writeValueAsString(object);
            Outbox outbox = new Outbox();
            outbox.setMessage(message);
            outbox.setStatus(MessageStatusEnum.STARTED);
            outboxDao.persist(outbox);
            return outbox.getId();
        } catch (Exception e) {
            logger.log(Level.WARNING, e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Transactional
    public void update(Outbox outbox) {
        outboxDao.update(outbox);
    }

    public Outbox findOne(UUID uuid) {
        return this.outboxDao.findOne(uuid);
    }

    public List<Outbox> findAll() {return this.outboxDao.findAll();}

    @Transactional
    public List<Outbox> findTopNWithStatus(int limit, MessageStatusEnum messageStatusEnum) {
        return this.outboxDao.findTopNWithStatus(limit, messageStatusEnum);
    }

    @Transactional
    public Outbox findByMessage(String message) {
        return this.outboxDao.findByMessage(message);
    }
}
